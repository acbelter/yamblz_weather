package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.integration.LoadWeatherTest;
import me.maximpestryakov.yamblzweather.ui.NavigationDrawerUiTest;
import me.maximpestryakov.yamblzweather.ui.WeatherUiTest;

@Singleton
@Component(modules = {AppModule.class})
public interface TestAppComponent extends AppComponent {
    void inject(NavigationDrawerUiTest test);
    void inject(WeatherUiTest test);
    void inject(LoadWeatherTest test);
}