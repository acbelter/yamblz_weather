package me.maximpestryakov.yamblzweather.data;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.di.DaggerTestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.ResReader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
// TODO Add additional parameters testing: wind, pressure...
// FIXME TEST DEPRECATED
public class WeatherApiTest {
    private static final float W_LATITUDE = -16.92f;
    private static final float W_LONGITUDE = 145.77f;
    private static final float F_LATITUDE = 34.966671f;
    private static final float F_LONGITUDE = 138.933334f;

    @Mock
    Context mockContext;
    @Inject
    WeatherApi weatherApi;
    @Inject
    NetworkUtil networkUtil;
    private ResReader resReader = new ResReader();
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        TestAppModule testAppModule = new TestAppModule(mockContext);
        mockWebServer = testAppModule.getMockWebServer();

        TestAppComponent component = DaggerTestAppComponent.builder()
                .appModule(testAppModule)
                .build();
        component.inject(this);

        Mockito.when(networkUtil.isConnected()).thenReturn(true);
    }

    @Test
    public void testGetWeatherSuccessResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resReader.readString("json/weather.json")));

        TestObserver<WeatherResult> observer =
                weatherApi.getWeather(W_LATITUDE, W_LONGITUDE, "en").test();
        observer.assertNoErrors();

        WeatherResult result = observer.values().get(0);

        assertThat(result.coord).isNotNull();
        assertThat(result.coord.lat).isEqualTo(W_LATITUDE);
        assertThat(result.coord.lon).isEqualTo(W_LONGITUDE);
        assertThat(result.dataTimestamp).isEqualTo(1435658272L);
        assertThat(result.main).isNotNull();
        assertThat(result.main.temp).isEqualTo(293.25f);
    }

    @Test
    public void testGetWeatherFailureResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        TestObserver<WeatherResult> observer =
                weatherApi.getWeather(W_LATITUDE, W_LONGITUDE, "en").test();
        observer.assertError(throwable -> true);
    }

    @Test
    public void testGetForecastSuccessResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resReader.readString("json/forecast.json")));

        TestObserver<ForecastResult> observer =
                weatherApi.getForecast(F_LATITUDE, F_LONGITUDE, "en").test();
        observer.assertNoErrors();

        ForecastResult result = observer.values().get(0);

        assertThat(result.forecast).isNotNull();
        assertThat(result.count).isEqualTo(1);
        assertThat(result.count).isEqualTo(result.forecast.size());

        assertThat(result.city).isNotNull();
        assertThat(result.city.coord).isNotNull();
        assertThat(result.city.coord.lat).isEqualTo(F_LATITUDE);
        assertThat(result.city.coord.lon).isEqualTo(F_LONGITUDE);

        ForecastItem forecast = result.forecast.get(0);
        assertThat(forecast.dataTimestamp).isEqualTo(1406106000L);
        assertThat(forecast.main).isNotNull();
        assertThat(forecast.main.temp).isEqualTo(298.77f);
    }

    @Test
    public void testGetForecastFailureResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        TestObserver<ForecastResult> observer =
                weatherApi.getForecast(F_LATITUDE, F_LONGITUDE, "en").test();
        observer.assertError(throwable -> true);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
