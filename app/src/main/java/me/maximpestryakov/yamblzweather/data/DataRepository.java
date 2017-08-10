package me.maximpestryakov.yamblzweather.data;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import io.reactivex.Maybe;
import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.FullWeatherData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;
import me.maximpestryakov.yamblzweather.data.model.DataConverter;

public class DataRepository {
    private DataConverter dataConverter;
    private WeatherDatabase database;
    private PlacesApi placesApi;
    private WeatherApi weatherApi;

    public DataRepository(DataConverter dataConverter,
                          WeatherDatabase database,
                          PlacesApi placesApi,
                          WeatherApi weatherApi) {
        this.dataConverter = dataConverter;
        this.database = database;
        this.placesApi = placesApi;
        this.weatherApi = weatherApi;
    }

    @RxLogObservable(RxLogObservable.Scope.STREAM)
    public Single<PlaceData> getPlaceData(String placeId,
                                          String lang,
                                          boolean forceNetwork) {
        Maybe<PlaceData> databaseData = database.getPlaceData(placeId)
                .filter(placeData -> placeData.lang.equals(lang));

        Maybe<PlaceData> networkData =
                placesApi.getPlaceData(placeId, lang)
                        .toMaybe()
                        .concatMap(result -> {
                            if (result.success()) {
                                return Maybe.just(dataConverter.convert(result, lang));
                            }
                            return Maybe.empty();
                        }).doOnSuccess(placeData -> database.insertPlaceData(placeData));
//                        })
//                        .concatMap(placeData -> database.insertPlaceData(placeData).toMaybe());

        if (forceNetwork) {
            return networkData.toSingle();
        } else {
            return Maybe.concat(databaseData, networkData).firstOrError();
        }
    }

    @RxLogObservable(RxLogObservable.Scope.STREAM)
    public Single<WeatherData> getWeatherData(String placeId,
                                              float lat,
                                              float lng,
                                              String lang,
                                              boolean forceNetwork) {
        Maybe<WeatherData> databaseData = database.getWeatherData(placeId);

        Maybe<WeatherData> networkData = weatherApi.getWeather(lat, lng, lang)
                .toMaybe()
                .concatMap(result -> {
                    if (result.success()) {
                        return Maybe.just(dataConverter.convert(result, placeId));
                    }
                    return Maybe.empty();
                }).concatMap(weatherData -> {
                    weatherData.weatherTimestamp = System.currentTimeMillis();
                    database.insertWeatherData(weatherData);
                    return Maybe.just(weatherData);
                });

        if (forceNetwork) {
            return networkData.toSingle();
        } else {
            return Maybe.concat(databaseData, networkData).firstOrError();
        }
    }

    @RxLogObservable(RxLogObservable.Scope.STREAM)
    public Single<ForecastData> getForecastData(String placeId,
                                                float lat,
                                                float lng,
                                                String lang,
                                                boolean forceNetwork) {
        Maybe<ForecastData> databaseData = database.getForecastData(placeId);

        Maybe<ForecastData> networkData = weatherApi.getForecast(lat, lng, lang)
                .toMaybe()
                .concatMap(result -> {
                    if (result.success()) {
                        return Maybe.just(dataConverter.convert(result, placeId));
                    }
                    return Maybe.empty();
                }).concatMap(forecastData -> {
                    forecastData.forecastTimestamp = System.currentTimeMillis();
                    database.insertForecastData(forecastData);
                    return Maybe.just(forecastData);
                });

        if (forceNetwork) {
            return networkData.toSingle();
        } else {
            return Maybe.concat(databaseData, networkData).firstOrError();
        }
    }

    public Single<FullWeatherData> getFullWeatherData(String placeId,
                                                      float lat,
                                                      float lng,
                                                      String lang,
                                                      boolean forceNetwork) {
        // TODO Change retry to retryWhen
        Single<WeatherData> weather =
                getWeatherData(placeId, lat, lng, lang, forceNetwork).retry(2);
        Single<ForecastData> forecast =
                getForecastData(placeId, lat, lng, lang, forceNetwork).retry(2);

        return Single.zip(weather, forecast, FullWeatherData::new);
    }
}
