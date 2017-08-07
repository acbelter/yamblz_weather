package me.maximpestryakov.yamblzweather.presentation.weather;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import me.maximpestryakov.yamblzweather.data.db.model.FullWeatherData;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;

interface WeatherView extends MvpView {
    void showPlaceSelectionUi();

    void showLoading(boolean loading);

    void showPlaceName(String name);

    void showPlacePredictions(List<Prediction> predictions);

    void showWeather(FullWeatherData weatherData);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(@StringRes int errorStrId);
}
