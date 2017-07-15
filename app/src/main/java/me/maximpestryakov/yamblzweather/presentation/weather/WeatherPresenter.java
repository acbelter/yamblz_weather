package me.maximpestryakov.yamblzweather.presentation.weather;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private static final int MOSCOW_ID = 524901;

    @Inject
    OpenWeatherMapService api;

    WeatherPresenter() {
        App.getAppComponent().inject(this);
        fetchWeather();
    }

    void onUpdateWeather() {
        fetchWeather();
    }

    private void fetchWeather() {
        api.getWeather(MOSCOW_ID, "metric", "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    double temp = weather.getMain().getTemp();
                    getViewState().showWeather(temp);
                }, throwable -> {
                    //
                });
    }
}
