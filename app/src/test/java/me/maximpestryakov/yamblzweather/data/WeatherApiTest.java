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
public class WeatherApiTest {
    @Mock
    Context mockContext;
    @Inject
    WeatherApi weatherApi;
    @Inject
    NetworkUtil networkUtil;
    private ResReader resReader;
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        TestAppModule testAppModule = new TestAppModule(mockContext);
        mockWebServer = testAppModule.getMockWebServer();
        resReader = new ResReader();

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

        TestObserver<WeatherResult> observer = weatherApi.getWeather(1, 1, "en").test();
        observer.assertNoErrors();

        WeatherResult result = observer.values().get(0);

        assertThat(result.dataTimestamp).isEqualTo(1435658272L);
        assertThat(result.name).isEqualTo("Cairns");
        assertThat(result.main).isNotNull();
        assertThat(result.main.temp).isEqualTo(293.25f);
    }

    @Test
    public void testGetWeatherFailureResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        TestObserver<WeatherResult> observer = weatherApi.getWeather(1, 1, "en").test();
        observer.assertError(throwable -> true);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
