package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.DataRepository;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import timber.log.Timber;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {
    @Inject
    Context context;
    @Inject
    PrefsRepository prefs;
    @Inject
    DataRepository dataRepository;
    @Inject
    WeatherApi weatherApi;
    @Inject
    PlacesApi placesApi;
    @Inject
    Gson gson;

    private String lang;
    private String currentPlaceId;

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
        lang = prefs.getLang();
        currentPlaceId = prefs.getPlaceId();
    }

    public void start() {
        Timber.d("Start weather presentation for place id: %s", currentPlaceId);
        if (currentPlaceId == null) {
            getViewState().showPlaceSelectionUi();
        } else {
            updatePlaceAndWeather(false, false);
        }
    }

    private void updatePlaceAndWeather(boolean forceUpdatePlace, boolean forceUpdateWeather) {
        getViewState().showLoading(true);
        dataRepository.getPlaceData(currentPlaceId, lang, forceUpdatePlace)
                .flatMap(placeData -> {
                    currentPlaceId = placeData.placeId;
                    prefs.setPlaceId(placeData.placeId);
                    return dataRepository.getFullWeatherData(
                            placeData.placeId, placeData.lat, placeData.lng, lang, forceUpdateWeather);

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullWeather -> {
                    getViewState().showLoading(false);
                    getViewState().showWeather(fullWeather);
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showLoading(false);
                    getViewState().showError(R.string.error_weather_api);
                });
    }

    public void refreshWeather() {
        if (currentPlaceId == null) {
            getViewState().showPlaceSelectionUi();
        } else {
            updatePlaceAndWeather(false, true);
        }
    }



}
