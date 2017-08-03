package me.maximpestryakov.yamblzweather.di;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.util.MockApiData;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.ResReader;
import me.maximpestryakov.yamblzweather.util.StringUtil;
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
    NetworkUtil provideNetworkUtil(Context context) {
        NetworkUtil util = mock(NetworkUtil.class);
        when(util.isConnected()).thenReturn(true);
        return util;
    }

    @Override
    StringUtil provideStringUtil(Context context) {
        StringUtil util = mock(StringUtil.class);
        String localWeather = new ResReader().readString("json/local_weather.json");
        try {
            when(util.readFromFile(anyString())).thenReturn(localWeather);
        } catch (IOException e) {
            // Ignore
        }
        return util;
    }
}
