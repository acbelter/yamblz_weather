package me.maximpestryakov.yamblzweather.di;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.GooglePlacesService;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;
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
    private Single<Weather> weatherSuccessMock;
    private Single<Weather> weatherFailureMock;
    private Single<PlacesPredictionsResult> placePredictionsSuccessMock;
    private Single<PlacesPredictionsResult> placePredictionsFailureMock;
    private Single<PlaceResult> placeSuccessMock;
    private Single<PlaceResult> placeFailureMock;

    private Single<Weather> weatherMock;
    private Single<PlacesPredictionsResult> placePredictionsMock;
    private Single<PlaceResult> placeMock;

    public TestAppModule(Context applicationContext) {
        super(applicationContext);
        weatherSuccessMock = MockApiData.createWeatherSuccessMock();
        weatherFailureMock = MockApiData.createWeatherFailureMock();
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
    OpenWeatherMapService provideOpenWeatherMapService(OkHttpClient client,
                                                       Gson gson) {
        OpenWeatherMapService service = mock(OpenWeatherMapService.class);
        when(service.getWeather(anyFloat(), anyFloat(), anyString()))
                .thenAnswer(invocation -> weatherMock);
        return service;
    }

    @Override
    GooglePlacesService provideGooglePlacesService(OkHttpClient client,
                                                   Gson gson) {
        GooglePlacesService service = mock(GooglePlacesService.class);
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
