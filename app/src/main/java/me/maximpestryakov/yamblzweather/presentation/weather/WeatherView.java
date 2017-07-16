package me.maximpestryakov.yamblzweather.presentation.weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import me.maximpestryakov.yamblzweather.data.model.Weather;

interface WeatherView extends MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showWeather(Weather weather);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setLoading(boolean loading);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(String message);
}
