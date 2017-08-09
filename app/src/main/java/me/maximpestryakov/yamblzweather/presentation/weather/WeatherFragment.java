package me.maximpestryakov.yamblzweather.presentation.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.FullWeatherData;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;

//import android.support.design.widget.Snackbar;

public class WeatherFragment extends Fragment implements WeatherView {
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

//    @InjectPresenter
//    WeatherPresenter presenter;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.temperature)
//    TextView temperatureText;
//    @BindView(R.id.time)
//    TextView timeText;

    private Unbinder unbinder;
    private Disposable placeTextDisposable;

    @NonNull
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        placeTextDisposable.dispose();
    }

    @Override
    public void showPlaceSelectionUi() {
//        Intent intent = new Intent(getActivity(), SelectPlaceActivity.class);
//        startActivity(intent);
    }

    @Override
    public void showLoading(boolean loading) {

    }

    @Override
    public void showPlaceName(String name) {

    }

    @Override
    public void showPlacePredictions(List<Prediction> predictions) {

    }

    @Override
    public void showWeather(FullWeatherData weatherData) {
//        Gson gson = new Gson();
//        WeatherResult weather = weatherData.getWeather().getParsedWeatherData(gson);
//        int temperature = Math.round(weather.main.temp);
//        temperatureText.setText(getString(R.string.temperature, temperature));
//        String time =  dateFormat.format(new Date(weather.dataTimestamp * 1000));
//        timeText.setText(getString(R.string.time, time));
    }

    @Override
    public void showError(int errorStrId) {
        if (getView() != null) {
            Snackbar.make(getView(), errorStrId, Snackbar.LENGTH_LONG).show();
        }
    }
}
