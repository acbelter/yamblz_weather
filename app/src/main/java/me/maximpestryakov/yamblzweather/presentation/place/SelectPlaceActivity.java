package me.maximpestryakov.yamblzweather.presentation.place;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.presentation.BaseActivity;
import me.maximpestryakov.yamblzweather.util.UiUtil;
import timber.log.Timber;

public class SelectPlaceActivity extends BaseActivity implements SelectPlaceView {
    private static final long TEXT_TYPE_DELAY = 300L;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.findPlaceText)
    AutoCompleteTextView findPlaceText;
    @BindView(R.id.favoritePlacesList)
    RecyclerView favoritePlacesList;
    @InjectPresenter
    SelectPlacePresenter presenter;

    private PlacesAdapter favoritePlacesAdapter;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        ButterKnife.bind(this);

        UiUtil.toolbar(this, toolbar, true);
        title.setText(R.string.select_place);

        Disposable findPlaceTextDisposable = RxTextView.textChanges(findPlaceText)
                .filter(s -> !findPlaceText.isPerformingCompletion())
                .debounce(TEXT_TYPE_DELAY, TimeUnit.MILLISECONDS)
                .skip(1)
                .subscribe(s -> {
                    Timber.d("Input: %s", s);
                    if (!TextUtils.isEmpty(s)) {
                        presenter.loadPlacePredictions(s.toString());
                    }
                });

        disposable = new CompositeDisposable(findPlaceTextDisposable);

        findPlaceText.setOnItemClickListener((parent, view1, position, id) -> {
            Prediction prediction = (Prediction) parent.getAdapter().getItem(position);
            presenter.selectPlacePrediction(prediction);
        });

        favoritePlacesList.setHasFixedSize(true);
        favoritePlacesList.setLayoutManager(new LinearLayoutManager(this));

        presenter.loadFavoritePlaces();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        disposable.dispose();
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
                close();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public void showFavoritePlaces(List<PlaceData> places) {
        favoritePlacesAdapter = new PlacesAdapter(places);
        favoritePlacesList.setAdapter(favoritePlacesAdapter);
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
    public void showError() {
        Snackbar.make(findViewById(android.R.id.content),
                R.string.error_place_predictions_api, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void close() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
