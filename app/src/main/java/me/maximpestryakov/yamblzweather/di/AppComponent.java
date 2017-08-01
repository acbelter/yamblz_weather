package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(WeatherPresenter weatherPresenter);
    void inject(SyncWeatherJob syncWeatherJob);
}
