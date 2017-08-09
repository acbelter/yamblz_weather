package me.maximpestryakov.yamblzweather.presentation.place;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import timber.log.Timber;

@InjectViewState
public class SelectPlacePresenter extends MvpPresenter<SelectPlaceView> {
    @Inject
    PlacesApi placesApi;
    @Inject
    PrefsRepository prefs;
    @Inject
    WeatherDatabase weatherDatabase;

    private String lang;
    private String currentPlaceId;

    private CompositeDisposable disposable;

    public SelectPlacePresenter() {
        App.getAppComponent().inject(this);
        lang = prefs.getLang();
        currentPlaceId = prefs.getPlaceId();
        disposable = new CompositeDisposable();
    }

    public void loadFavoritePlaces() {
        Disposable placesDisposable = weatherDatabase.getAllPlaceData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoritePlaces -> {
                    getViewState().showFavoritePlaces(favoritePlaces);
                }, Timber::d);

        disposable.add(placesDisposable);
    }

    public void destroy() {
        disposable.dispose();
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
                        getViewState().showError();
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showPlacePredictions(new ArrayList<>(0));
                    getViewState().showError();
                });
    }

    public void selectPlacePrediction(Prediction prediction) {
        Timber.d("Selected place prediction: %s", prediction);
        currentPlaceId = prediction.placeId;
        prefs.setPlaceId(currentPlaceId);
//        updatePlaceAndWeather(true, true);
    }
}
