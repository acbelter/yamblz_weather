package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import timber.log.Timber;

public class SyncWeatherJob extends Job {
    public static final String TAG = "SyncWeatherJob";

    @Inject
    Context context;
    @Inject
    PrefsRepository prefs;
    @Inject
    DataRepository dataRepository;
    @Inject
    WeatherApi weatherApi;
    @Inject
    Gson gson;
    @Inject
    NetworkUtil networkUtil;

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

        final String placeId = prefs.getPlaceId();
        if (placeId == null) {
            return Result.FAILURE;
        }

        final String lang = prefs.getLang();
        updateFullWeather(placeId, lang);

        return Result.SUCCESS;
    }

    private void updateFullWeather(String placeId, String lang) {
        dataRepository.getPlaceData(placeId, lang, false)
                .flatMap(placeData -> dataRepository.getFullWeatherData(
                        placeData.placeId, placeData.lat, placeData.lng, lang, true))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(fullWeather -> {
                    Timber.d("Weather and forecast are updated");
                }, Timber::d);
    }
}
