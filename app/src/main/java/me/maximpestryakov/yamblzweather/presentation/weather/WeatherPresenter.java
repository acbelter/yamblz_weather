package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.util.StringUtil;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private static final int MOSCOW_ID = 524901;

    @Inject
    Context context;

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
        getViewState().setLoading(true);
        api.getWeather(MOSCOW_ID, "metric", context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    double temp = weather.getMain().getTemp();
                    getViewState().setLoading(false);
                    getViewState().showWeather(temp);
                }, throwable -> {
                    getViewState().setLoading(false);
                    getViewState().showError(StringUtil.getErrorMessage(throwable));
                });
    }
}
