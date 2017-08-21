package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.presentation.place.SelectPlacePresenter;
import me.maximpestryakov.yamblzweather.presentation.settings.SettingsPresenter;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherActivity;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherPresenter;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.DetailedForecastsAdapter;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.FullForecastsAdapter;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralForecastsAdapter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(SyncWeatherJob job);

    void inject(WeatherActivity activity);

    void inject(FullForecastsAdapter adapter);

    void inject(GeneralForecastsAdapter adapter);

    void inject(DetailedForecastsAdapter adapter);

    void inject(SettingsPresenter presenter);

    void inject(SelectPlacePresenter presenter);

    void inject(WeatherPresenter presenter);
}
