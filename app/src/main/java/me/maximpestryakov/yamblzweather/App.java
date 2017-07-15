package me.maximpestryakov.yamblzweather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import me.maximpestryakov.yamblzweather.di.AppComponent;
import me.maximpestryakov.yamblzweather.di.AppModule;
import me.maximpestryakov.yamblzweather.di.DaggerAppComponent;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static AppComponent appComponent;

    public static Context getContext() {
        return context;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }
}
