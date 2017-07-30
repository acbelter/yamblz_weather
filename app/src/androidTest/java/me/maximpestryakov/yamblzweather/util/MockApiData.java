package me.maximpestryakov.yamblzweather.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;

public final class MockApiData {
    private static final long DELAY = 0L;
    private static final TimeUnit DELAY_TIME_UNIT = TimeUnit.SECONDS;
    private static ResReader resReader = new ResReader();
    private static Gson gson = new Gson();

    private MockApiData() {}

    public static Single<Weather> createWeatherSuccessMock() {
        return Single.just(gson.fromJson(
                resReader.readString("json/weather.json"), Weather.class))
                .delay(DELAY, DELAY_TIME_UNIT);
    }

    public static Single<Weather> createWeatherFailureMock() {
        return Single.<Weather>error(
                new IOException("Fake server error while loading weather"))
                .delay(DELAY, DELAY_TIME_UNIT);
    }

    public static Single<PlacesPredictionsResult> createPlacePredictionsSuccessMock() {
        return Single.just(gson.fromJson(
                resReader.readString("json/place_predictions.json"), PlacesPredictionsResult.class))
                .delay(DELAY, DELAY_TIME_UNIT);
    }

    public static Single<PlacesPredictionsResult> createPlacePredictionsFailureMock() {
        return Single.<PlacesPredictionsResult>error(
                new IOException("Fake server error while loading place predictions"))
                .delay(DELAY, DELAY_TIME_UNIT);
    }

    public static Single<PlaceResult> createPlaceSuccessMock() {
        return Single.just(gson.fromJson(
                resReader.readString("json/place_data.json"), PlaceResult.class))
                .delay(DELAY, DELAY_TIME_UNIT);
    }

    public static Single<PlaceResult> createPlaceFailureMock() {
        return Single.<PlaceResult>error(
                new IOException("Fake server error while loading place data"))
                .delay(DELAY, DELAY_TIME_UNIT);
    }
}
