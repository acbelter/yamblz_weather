package me.maximpestryakov.yamblzweather.di;

import javax.inject.Singleton;

import dagger.Component;
import me.maximpestryakov.yamblzweather.data.GooglePlacesServiceTest;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapServiceTest;

@Singleton
@Component(modules = {AppModule.class})
public interface TestAppComponent extends AppComponent {
    void inject(OpenWeatherMapServiceTest test);
    void inject(GooglePlacesServiceTest test);
}