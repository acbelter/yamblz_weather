package me.maximpestryakov.yamblzweather;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import me.maximpestryakov.yamblzweather.data.PreferencesStorage;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJobCreator;
import me.maximpestryakov.yamblzweather.di.AppComponent;
import me.maximpestryakov.yamblzweather.di.AppModule;
import me.maximpestryakov.yamblzweather.di.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initAppComponent();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        JobManager.create(this).addJobCreator(new SyncWeatherJobCreator());

        PreferencesStorage prefs = new PreferencesStorage(this, new Gson());
        if (prefs.isWeatherScheduleEnabled()) {
            SyncWeatherJob.schedule(prefs.getWeatherScheduleInterval());
        }
    }
}
