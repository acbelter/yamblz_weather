package me.maximpestryakov.yamblzweather.presentation.weather;

import com.arellomobile.mvp.MvpView;

interface WeatherView extends MvpView {

    void showWeather(double temp);
}
