package me.maximpestryakov.yamblzweather.presentation.place;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.DataRepository;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
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
    @Inject
    DataRepository dataRepository;

    private String lang;
    private String currentPlaceId;

    private Disposable placePredictionsDisposable;
    private CompositeDisposable disposables;

    public SelectPlacePresenter() {
        App.getAppComponent().inject(this);
        lang = prefs.getLang();
        currentPlaceId = prefs.getPlaceId();
        disposables = new CompositeDisposable();
    }

    public void start() {
        if (disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
    }

    public void stop() {
        if (placePredictionsDisposable != null) {
            placePredictionsDisposable.dispose();
            placePredictionsDisposable = null;
        }
        disposables.dispose();
    }

    public void loadFavoritePlaces() {
        Disposable favoritePlacesDisposable = weatherDatabase.getAllPlaceData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoritePlaces -> {
                    getViewState().showFavoritePlaces(favoritePlaces);
                }, Timber::d);

        disposables.add(favoritePlacesDisposable);
    }

    public void loadPlacePredictions(String input) {
        if (placePredictionsDisposable != null) {
            placePredictionsDisposable.dispose();
        }

        placePredictionsDisposable = placesApi.getPlacePredictions(input, lang)
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

    public void selectPlacePrediction(Prediction prediction) {
        Timber.d("Selected place prediction: %s", prediction);
        currentPlaceId = prediction.placeId;
        prefs.setPlaceId(currentPlaceId);

        getViewState().showLoading(true);
        Disposable placeDataDisposable = dataRepository.getPlaceData(prediction.placeId, lang, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeData -> {
                    Timber.d("Obtained place data %s", placeData);
                    getViewState().showLoading(false);
                    getViewState().close(placeData);
                }, throwable -> {
                    Timber.d(throwable);
                    getViewState().showLoading(false);
                    getViewState().showError(R.string.error_place_api);
                });
        disposables.add(placeDataDisposable);
    }

    public void removePlace(PlaceData place) {
        Disposable removePlaceDisposable =
                Completable
                        .create(e -> {
                            weatherDatabase.deletePlaceData(place.placeId);
                            e.onComplete();
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Timber.d("Removed place: " + place.placeName), Timber::d);
        disposables.add(removePlaceDisposable);
    }
}
