package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.data.model.Weather;
import me.maximpestryakov.yamblzweather.util.StringUtil;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private static final int MOSCOW_ID = 524901;

    private static final String FILE_NAME = "weather.json";

    @Inject
    Context context;

    @Inject
    OpenWeatherMapService api;

    @Inject
    Gson gson;

    WeatherPresenter() {
        App.getAppComponent().inject(this);
        fetchWeather();
    }

    void onUpdateWeather() {
        fetchWeather();
    }

    private void fetchWeather() {
        getViewState().setLoading(true);
        Weather localWeather = null;
        try {
            String json = StringUtil.readFromFile(FILE_NAME);
            localWeather = gson.fromJson(json, Weather.class);
        } catch (IOException e) {
            // empty
        }
        if (localWeather != null) {
            getViewState().showWeather(localWeather);
        }
        api.getWeather(MOSCOW_ID, "metric", context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    getViewState().setLoading(false);
                    getViewState().showWeather(weather);
                    StringUtil.writeToFile(FILE_NAME, gson.toJson(weather));
                }, throwable -> {
                    throwable.printStackTrace();
                    getViewState().setLoading(false);
                    getViewState().showError(StringUtil.getErrorMessage(throwable));
                });
    }
}
