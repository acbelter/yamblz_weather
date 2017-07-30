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

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
        placeName = prefs.getPlaceName();
        placeId = prefs.getPlaceId();
        place = prefs.getPlace();
    }

    public void start() {
        getViewState().showPlaceName(placeName);
    }

    public void refreshData() {
        if (place != null) {
            fetchWeather(place, false);
        } else if (placeId != null) {
            fetchPlace(placeId);
        }
    }

    public void processPlacePredictionSelection(Prediction prediction) {
        Timber.d("Selected place prediction: %s", prediction);
        placeName = prediction.getDescription();
        prefs.setPlaceName(placeName);

        placeId = prediction.getPlaceId();
        prefs.setPlaceId(placeId);
        fetchPlace(placeId);
    }

    public void onUpdateWeather() {
        if (place != null) {
            fetchWeather(place, true);
        }
    }

    public void fetchPlacePredictions(String input) {
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
                    Timber.d(throwable);
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
                        prefs.setPlace(place);
                        fetchWeather(place, true);
                    } else {
                        getViewState().showError(R.string.error_place_api);
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showError(R.string.error_place_api);
                });
    }

    private void fetchWeather(Place place, boolean forceRefresh) {
        Timber.d("Start fetch weather for place %s: ", place.getVicinity());
        getViewState().setLoading(true);
        Weather localWeather = null;
        try {
            String json = stringUtil.readFromFile(FILE_NAME);
            Timber.d("Fetch weather from file: %s", json);
            localWeather = gson.fromJson(json, Weather.class);
        } catch (IOException e) {
            // empty
        }

        if (localWeather == null) {
            fetchWeather(place);
        } else if (forceRefresh) {
            fetchWeather(place);
        } else {
            getViewState().showWeather(localWeather);
            getViewState().setLoading(false);
        }
    }

    private void fetchWeather(Place place) {
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
                    Timber.d(throwable);
                    getViewState().setLoading(false);
                    getViewState().showError(R.string.error_weather_api);
                });
    }
}
