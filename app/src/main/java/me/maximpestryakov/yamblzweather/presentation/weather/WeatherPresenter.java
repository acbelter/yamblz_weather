package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.DataRepository;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.util.NoInternetException;
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

    private Disposable updateDisposable;

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
        lang = prefs.getLang();
        currentPlaceId = prefs.getPlaceId();
    }

    public void stop() {
        if (updateDisposable != null) {
            updateDisposable.dispose();
        }
    }

    public void updateCurrentPlace(String placeId) {
        Timber.d("Update current place id: %s", placeId);
        currentPlaceId = placeId;
        prefs.setPlaceId(currentPlaceId);
    }

    public void updateCurrentPlaceWeather() {
        Timber.d("Update weather for current place id: %s", currentPlaceId);
        if (currentPlaceId == null) {
            getViewState().requestPlaceSelection();
        } else {
            getWeatherForPlace(false, true);
        }
    }

    private void getWeatherForPlace(boolean forceUpdatePlace, boolean forceUpdateWeather) {
        getViewState().showLoading(true);

        if (updateDisposable != null) {
            updateDisposable.dispose();
        }

        updateDisposable = dataRepository.getPlaceData(currentPlaceId, lang, forceUpdatePlace)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(placeData -> getViewState().showPlaceName(placeData.placeName))
                .observeOn(Schedulers.io())
                .flatMap(placeData -> dataRepository.getFullWeatherData(
                        placeData.placeId, placeData.lat, placeData.lng, lang, forceUpdateWeather))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullWeather -> {
                    getViewState().showLoading(false);
                    getViewState().showWeather(fullWeather);
                    if (forceUpdateWeather && fullWeather.isFromCache()) {
                        getViewState().showError(R.string.error_no_internet);
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showLoading(false);
                    if (throwable instanceof NoInternetException) {
                        getViewState().showError(R.string.error_no_internet);
                    } else {
                        getViewState().showError(R.string.error_weather_api);
                    }
                });
    }
}
