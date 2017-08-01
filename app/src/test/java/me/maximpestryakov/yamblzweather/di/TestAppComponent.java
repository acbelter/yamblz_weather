package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.data.GooglePlacesApiTest;
import me.maximpestryakov.yamblzweather.data.WeatherApiTest;

@Singleton
@Component(modules = {AppModule.class})
public interface TestAppComponent extends AppComponent {
    void inject(WeatherApiTest test);
    void inject(GooglePlacesApiTest test);
}