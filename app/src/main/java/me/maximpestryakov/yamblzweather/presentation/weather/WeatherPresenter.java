package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.GooglePlacesService;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.data.PreferencesStorage;
import me.maximpestryakov.yamblzweather.data.model.place.Location;
import me.maximpestryakov.yamblzweather.data.model.place.Place;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;
import me.maximpestryakov.yamblzweather.util.StringUtil;
import timber.log.Timber;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {
    private static final String FILE_NAME = "weather.json";

    @Inject
    Context context;

    @Inject
    PreferencesStorage prefs;

    @Inject
    OpenWeatherMapService weatherApi;

    @Inject
    GooglePlacesService placesApi;

    @Inject
    Gson gson;

    @Inject
    StringUtil stringUtil;

    private String placeName;
    private String placeId;
    private Place place;

    WeatherPresenter() {
        App.getAppComponent().inject(this);
        placeName = prefs.loadPlaceName();
        placeId = prefs.loadPlaceId();
        place = prefs.loadPlace();
    }

    void start() {
        getViewState().showPlaceName(placeName);
    }

    void refreshData() {
        if (place != null) {
            fetchWeather(place);
        } else if (placeId != null) {
            fetchPlace(placeId);
        }
    }

    void processPlacePredictionSelection(Prediction prediction) {
        Timber.d("Selected place prediction: %s", prediction);
        placeName = prediction.getDescription();
        prefs.savePlaceName(placeName);

        placeId = prediction.getPlaceId();
        prefs.savePlaceId(placeId);
        fetchPlace(placeId);
    }

    void onUpdateWeather() {
        if (place != null) {
            fetchWeather(place);
        }
    }

    void fetchPlacePredictions(String input) {
        placesApi.getPlacePredictions(input, context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Timber.d("Find predictions status: %s", result.getStatus());
                    if ("OK".equals(result.getStatus()) ||
                            "ZERO_RESULTS".equals(result.getStatus())) {
                        getViewState().showPlacePredictions(result.getPredictions());
                    } else {
                        getViewState().showError(R.string.error_place_predictions_api);
                    }
                }, throwable -> {
                    getViewState().showPlacePredictions(new ArrayList<>(0));
                    getViewState().showError(R.string.error_place_predictions_api);
                });
    }

    private void fetchPlace(String placeId) {
        placesApi.getPlaceData(placeId, context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if ("OK".equals(result.getStatus())) {
                        place = result.getPlace();
                        prefs.savePlace(place);
                        fetchWeather(place);
                    } else {
                        getViewState().showError(R.string.error_place_api);
                    }
                }, throwable -> {
                    getViewState().showError(R.string.error_place_api);
                });
    }

    private void fetchWeather(Place place) {
        Timber.d("Start fetch weather: " + place.getVicinity());
        getViewState().setLoading(true);
        Weather localWeather = null;
        try {
            String json = stringUtil.readFromFile(FILE_NAME);
            Timber.d("Fetch weather from file: %s", json);
            localWeather = gson.fromJson(json, Weather.class);
        } catch (IOException e) {
            // empty
        }
        if (localWeather != null) {
            getViewState().showWeather(localWeather);
        }

        Location location = place.getGeometry().getLocation();
        weatherApi.getWeather(location.getLat(), location.getLng(), context.getString(R.string.lang))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    String json = gson.toJson(weather);
                    Timber.d("Fetch from network: %s", json);
                    getViewState().setLoading(false);
                    getViewState().showWeather(weather);
                    stringUtil.writeToFile(FILE_NAME, json);
                }, throwable -> {
                    getViewState().setLoading(false);
                    getViewState().showError(R.string.error_weather_api);
                });
    }
}
