package me.maximpestryakov.yamblzweather.presentation.weather;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;

interface WeatherView extends MvpView {
    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showWeather(Weather weather);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setLoading(boolean loading);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(@StringRes int errorStrId);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showPlaceName(String name);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showPlacePredictions(List<Prediction> predictions);
}
