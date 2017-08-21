package me.maximpestryakov.yamblzweather.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.db.AppDatabase;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.util.MockApiData;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import okhttp3.OkHttpClient;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAppModule extends AppModule {
    private Single<WeatherResult> weatherSuccessMock;
    private Single<WeatherResult> weatherFailureMock;
    private Single<ForecastResult> forecastSuccessMock;
    private Single<ForecastResult> forecastFailureMock;
    private Single<PlacesPredictionsResult> placePredictionsSuccessMock;
    private Single<PlacesPredictionsResult> placePredictionsFailureMock;
    private Single<PlaceResult> placeSuccessMock;
    private Single<PlaceResult> placeFailureMock;

    private Single<WeatherResult> weatherMock;
    private Single<ForecastResult> forecastMock;
    private Single<PlacesPredictionsResult> placePredictionsMock;
    private Single<PlaceResult> placeMock;

    public TestAppModule(Context applicationContext) {
        super(applicationContext);
        weatherSuccessMock = MockApiData.createWeatherSuccessMock();
        weatherFailureMock = MockApiData.createWeatherFailureMock();
        forecastSuccessMock = MockApiData.createForecastSuccessMock();
        forecastFailureMock = MockApiData.createForecastFailureMock();
        placePredictionsSuccessMock = MockApiData.createPlacePredictionsSuccessMock();
        placePredictionsFailureMock = MockApiData.createPlacePredictionsFailureMock();
        placeSuccessMock = MockApiData.createPlaceSuccessMock();
        placeFailureMock = MockApiData.createPlaceFailureMock();
    }

    public void useWeatherSuccessMock() {
        weatherMock = weatherSuccessMock;
    }

    public void useWeatherFailureMock() {
        weatherMock = weatherFailureMock;
    }

    public void useForecastSuccessMock() {
        forecastMock = forecastSuccessMock;
    }

    public void useForecastFailureMock() {
        forecastMock = forecastFailureMock;
    }

    public void usePlacePredictionsSuccessMock() {
        placePredictionsMock = placePredictionsSuccessMock;
    }

    public void usePlacePredictionsFailureMock() {
        placePredictionsMock = placePredictionsFailureMock;
    }

    public void usePlaceSuccessMock() {
        placeMock = placeSuccessMock;
    }

    public void usePlaceFailureMock() {
        placeMock = placeFailureMock;
    }

    @Override
    WeatherApi provideOpenWeatherMapService(OkHttpClient client,
                                            Gson gson) {
        WeatherApi service = mock(WeatherApi.class);
        when(service.getWeather(anyFloat(), anyFloat(), anyString()))
                .thenAnswer(invocation -> weatherMock);
        when(service.getForecast(anyFloat(), anyFloat(), anyString()))
                .thenAnswer(invocation -> forecastMock);
        return service;
    }

    @Override
    PlacesApi provideGooglePlacesService(OkHttpClient client,
                                         Gson gson) {
        PlacesApi service = mock(PlacesApi.class);
        when(service.getPlacePredictions(anyString(), anyString()))
                .thenAnswer(invocation -> placePredictionsMock);
        when(service.getPlaceData(anyString(), anyString()))
                .thenAnswer(invocation -> placeMock);
        return service;
    }

    @Override
    WeatherDatabase provideDatabase(Context context) {
        AppDatabase db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        return db.weatherDatabase();
    }

    @Override
    NetworkUtil provideNetworkUtil(Context context) {
        NetworkUtil util = mock(NetworkUtil.class);
        when(util.isConnected()).thenReturn(true);
        return util;
    }
}
