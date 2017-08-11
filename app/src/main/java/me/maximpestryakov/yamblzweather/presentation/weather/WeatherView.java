package me.maximpestryakov.yamblzweather.presentation.weather;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import me.maximpestryakov.yamblzweather.data.db.model.FullWeatherData;

@StateStrategyType(AddToEndSingleStrategy.class)
interface WeatherView extends MvpView {
    @StateStrategyType(value = SkipStrategy.class)
    void requestPlaceSelection();

    void showLoading(boolean loading);

    void showPlaceName(String name);

    void showWeather(FullWeatherData data);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(@StringRes int errorStrId);
}
