package me.maximpestryakov.yamblzweather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.evernote.android.job.JobManager;

import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJobCreator;
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
                .appModule(new AppModule(context))
                .build();

        JobManager.create(context).addJobCreator(new SyncWeatherJobCreator());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getBoolean("weather_schedule_enabled", false)) {
            int interval = Integer.valueOf(sharedPreferences.getString("weather_schedule_interval", "30"));
            SyncWeatherJob.schedule(interval);
        }
    }
}
