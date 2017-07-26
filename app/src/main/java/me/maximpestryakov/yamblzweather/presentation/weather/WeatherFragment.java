package me.maximpestryakov.yamblzweather.presentation.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;
import timber.log.Timber;

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {
    private static final long TEXT_TYPE_DELAY = 300;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @InjectPresenter
    WeatherPresenter weatherPresenter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.placeText)
    AutoCompleteTextView placeText;
    private Unbinder unbinder;
    private Disposable placeTextDisposable;

    @NonNull
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(weatherPresenter::onUpdateWeather);
        placeTextDisposable = RxTextView.textChanges(placeText)
                .filter(s -> !placeText.isPerformingCompletion())
                .debounce(TEXT_TYPE_DELAY, TimeUnit.MILLISECONDS)
                .skip(1)
                .subscribe(s -> {
                    Timber.d("Input: %s", s);
                    if (!TextUtils.isEmpty(s)) {
                        weatherPresenter.fetchPlacePredictions(s.toString());
                    }
                });

        placeText.setOnItemClickListener((parent, view1, position, id) -> {
            Prediction prediction = (Prediction) parent.getAdapter().getItem(position);
            weatherPresenter.processPlacePredictionSelection(prediction);
        });

        weatherPresenter.start();
        if (savedInstanceState == null) {
            weatherPresenter.refreshData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.nav_weather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        placeTextDisposable.dispose();
    }

    @Override
    public void showWeather(Weather weather) {
        temperature.setText(getString(R.string.temperature, Math.round(weather.getMain().getTemperature())));
        time.setText(getString(R.string.time, dateFormat.format(new Date(weather.getTime() * 1000))));
    }

    @Override
    public void setLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void showError(int errorStrId) {
        if (getView() != null) {
            Snackbar.make(getView(), errorStrId, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showPlaceName(String name) {
        placeText.setText(name);
    }

    @Override
    public void showPlacePredictions(List<Prediction> predictions) {
        ArrayAdapter<Prediction> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, predictions);
        placeText.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (Prediction p : predictions) {
            Timber.d("\t%s", p.getDescription());
        }
    }
}
