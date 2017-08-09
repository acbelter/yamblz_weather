package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.presentation.place.SelectPlacePresenter;
import me.maximpestryakov.yamblzweather.presentation.settings.SettingsPresenter;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(SettingsPresenter presenter);

    void inject(SelectPlacePresenter presenter);

    void inject(WeatherPresenter presenter);

    void inject(SyncWeatherJob job);
}
