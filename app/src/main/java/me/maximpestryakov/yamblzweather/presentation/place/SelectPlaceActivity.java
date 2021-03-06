package me.maximpestryakov.yamblzweather.presentation.place;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.presentation.BaseActivity;
import me.maximpestryakov.yamblzweather.presentation.Consts;
import me.maximpestryakov.yamblzweather.util.UiUtil;
import timber.log.Timber;

public class SelectPlaceActivity extends BaseActivity implements
        SelectPlaceView, PlacesAdapter.AdapterCallback {
    private static final long TEXT_TYPE_DELAY = 300L;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.findPlaceText)
    AutoCompleteTextView findPlaceText;
    @BindView(R.id.placesList)
    RecyclerView placesList;
    @InjectPresenter
    SelectPlacePresenter presenter;

    private boolean isSelectFirstPlace;

    private PlacesAdapter placesAdapter;
    private CompositeDisposable disposables;

    private AlertDialog progress;

    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        ButterKnife.bind(this);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (savedInstanceState == null) {
            isSelectFirstPlace = getIntent().getBooleanExtra(Consts.KEY_SELECT_FIRST_PLACE, false);
        } else {
            isSelectFirstPlace = savedInstanceState.getBoolean(Consts.KEY_SELECT_FIRST_PLACE);
        }

        UiUtil.toolbar(this, toolbar, !isSelectFirstPlace);
        title.setText(R.string.select_place);

        Disposable findPlaceDisposable = RxTextView.textChanges(findPlaceText)
                .filter(s -> s.length() > 0 && !findPlaceText.isPerformingCompletion())
                .debounce(TEXT_TYPE_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(s -> {
                    Timber.d("Input: %s", s);
                    if (!TextUtils.isEmpty(s)) {
                        presenter.loadPlacePredictions(s.toString());
                    }
                });

        disposables = new CompositeDisposable(findPlaceDisposable);

        findPlaceText.setOnItemClickListener((parent, view1, position, id) -> {
            Prediction prediction = (Prediction) parent.getAdapter().getItem(position);
            presenter.selectPlacePrediction(prediction);
        });

        placesList.setHasFixedSize(true);
        placesList.setLayoutManager(new LinearLayoutManager(this));

        presenter.start();
        presenter.loadPlaces();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Consts.KEY_SELECT_FIRST_PLACE, isSelectFirstPlace);
    }

    @Override
    protected void onStart() {
        Timber.d("Start select place activity");
        super.onStart();
        if (placesAdapter != null) {
            placesAdapter.setAdapterCallback(this);
        }
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (placesAdapter != null) {
            placesAdapter.removeAdapterCallback();
        }
        presenter.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        UiUtil.tintToolbarIcons(toolbar, R.color.colorToolbarContent);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                close(null);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelectFirstPlace) {
            setResult(RESULT_CANCELED);
            ActivityCompat.finishAffinity(this);
        } else {
            close(null);
        }
    }

    @Override
    public void onItemRemoved(PlaceData place) {
        presenter.removePlace(place);
    }

    @Override
    public void onItemClicked(PlaceData place) {
        close(place);
    }

    @Override
    public void showPlaces(List<PlaceData> places) {
        placesAdapter = new PlacesAdapter(places);
        placesAdapter.setAdapterCallback(this);
        placesList.setAdapter(placesAdapter);
    }

    @Override
    public void showPlacePredictions(List<Prediction> predictions) {
        ArrayAdapter<Prediction> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, predictions);
        findPlaceText.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (Prediction p : predictions) {
            Timber.d("\t%s", p.description);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void showLoading(boolean loading) {
        inputMethodManager.hideSoftInputFromWindow(findPlaceText.getWindowToken(), 0);
        if (loading) {
            if (progress == null) {
                progress = new SpotsDialog(this);
                progress.show();
                progress.setMessage(getString(R.string.updating));
            } else {
                if (!progress.isShowing()) {
                    progress.show();
                }
            }
        } else {
            if (progress != null) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void showError(@StringRes int errorStrId) {
        Snackbar.make(findViewById(android.R.id.content), errorStrId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void close(PlaceData selectedPlace) {
        if (selectedPlace != null) {
            Intent data = new Intent();
            data.putExtra(Consts.KEY_SELECTED_PLACE, selectedPlace);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
        overridePendingTransition(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
    }
}
