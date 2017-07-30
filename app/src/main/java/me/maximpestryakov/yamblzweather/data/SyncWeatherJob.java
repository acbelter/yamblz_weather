package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.place.Location;
import me.maximpestryakov.yamblzweather.data.model.place.Place;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.StringUtil;

public class SyncWeatherJob extends Job {

    public static final String TAG = "SyncWeatherJob";

    private static final String FILE_NAME = "weather.json";

    @Inject
    Context context;

    @Inject
    PreferencesStorage prefs;

    @Inject
    OpenWeatherMapService api;

    @Inject
    Gson gson;

    @Inject
    NetworkUtil networkUtil;

    @Inject
    StringUtil stringUtil;

    SyncWeatherJob() {
        App.getAppComponent().inject(this);
    }

    public static void schedule(int periodicMinutes) {
        new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(periodicMinutes))
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        if (!networkUtil.isConnected()) {
            return Result.FAILURE;
        }

        Place place = prefs.getPlace();
        if (place == null) {
            return Result.FAILURE;
        }

        Location location = place.getGeometry().getLocation();
        api.getWeather(location.getLat(), location.getLng(), context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    stringUtil.writeToFile(FILE_NAME, gson.toJson(weather));
                }, throwable -> {
                    // empty
                });

        return Result.SUCCESS;
    }
}
