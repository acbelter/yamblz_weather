package me.maximpestryakov.yamblzweather;

import android.app.Application;

import com.evernote.android.job.JobManager;

import javax.inject.Inject;

import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJobCreator;
import me.maximpestryakov.yamblzweather.di.AppComponent;
import me.maximpestryakov.yamblzweather.di.AppModule;
import me.maximpestryakov.yamblzweather.di.DaggerAppComponent;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    private static AppComponent appComponent;

    @Inject
    PrefsRepository prefs;

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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Thin.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        appComponent = initAppComponent();

        appComponent.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        JobManager.create(this).addJobCreator(new SyncWeatherJobCreator());

        if (prefs.isUpdateBySchedule()) {
            SyncWeatherJob.schedule(prefs.getUpdateInterval());
        }
    }
}
