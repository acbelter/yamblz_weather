package me.maximpestryakov.yamblzweather;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.evernote.android.job.JobManager;

import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJobCreator;
import me.maximpestryakov.yamblzweather.di.AppComponent;
import me.maximpestryakov.yamblzweather.di.AppModule;
import me.maximpestryakov.yamblzweather.di.DaggerAppComponent;

public class App extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        JobManager.create(this).addJobCreator(new SyncWeatherJobCreator());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("weather_schedule_enabled", false)) {
            int interval = Integer.valueOf(sharedPreferences.getString("weather_schedule_interval", "30"));
            SyncWeatherJob.schedule(interval);
        }
    }
}
