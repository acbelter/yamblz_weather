package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.DataRepository;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
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

    public void processPlacePredictionSelection(Prediction prediction) {
        Timber.d("Selected place prediction: %s", prediction);
//        placeName = prediction.description;
//        prefs.setPlaceName(placeName);

        currentPlaceId = prediction.placeId;
        prefs.setPlaceId(currentPlaceId);
        updatePlaceAndWeather(true, true);
    }

    public void loadPlacePredictions(String input) {
        placesApi.getPlacePredictions(input, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(predictionsResult -> {
                    Timber.d("Find predictions status: %s", predictionsResult.status);
                    if (predictionsResult.success()) {
                        getViewState().showPlacePredictions(predictionsResult.predictions);
                    } else {
                        getViewState().showError(R.string.error_place_predictions_api);
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showPlacePredictions(new ArrayList<>(0));
                    getViewState().showError(R.string.error_place_predictions_api);
                });
    }
}
