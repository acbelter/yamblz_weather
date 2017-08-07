package me.maximpestryakov.yamblzweather.db;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import me.maximpestryakov.TestApp;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;
import me.maximpestryakov.yamblzweather.util.DbTestUtils;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Inject
    WeatherDatabase weatherDatabase;

    @Before
    public void setUp() {
        TestApp app = TestUtils.getTestApp(getTargetContext());
        app.getTestAppComponent().inject(this);
    }

    @Test
    public void testPlaceReadWrite() {
        PlaceData place1 = DbTestUtils.createPlace1();
        weatherDatabase.insertPlaceData(place1);

        TestObserver<PlaceData> observer = weatherDatabase.getPlaceData(place1.placeId).test();
        observer.assertNoErrors();

        PlaceData result = observer.values().get(0);
        DbTestUtils.assertPlace(place1, result);
    }

    @Test
    public void testPlaceReadWriteAll() {
        PlaceData place1 = DbTestUtils.createPlace1();
        PlaceData place2 = DbTestUtils.createPlace2();
        weatherDatabase.insertPlaceData(place1);
        weatherDatabase.insertPlaceData(place2);

        TestObserver<List<PlaceData>> observer = weatherDatabase.getAllPlaceData().test();
        observer.assertNoErrors();

        List<PlaceData> places = observer.values().get(0);
        DbTestUtils.assertPlace(place1, places.get(0));
        DbTestUtils.assertPlace(place2, places.get(1));
    }

    @Test
    public void testWeatherReadWrite() {
        WeatherData weather1 = DbTestUtils.createWeather1();
        weatherDatabase.insertWeatherData(weather1);

        TestObserver<WeatherData> observer = weatherDatabase.getWeatherData(weather1.placeId).test();
        observer.assertNoErrors();

        WeatherData result = observer.values().get(0);
        DbTestUtils.assertWeather(weather1, result);
    }

    @Test
    public void testWeatherReadWriteAll() {
        WeatherData weather1 = DbTestUtils.createWeather1();
        WeatherData weather2 = DbTestUtils.createWeather2();
        weatherDatabase.insertWeatherData(weather1);
        weatherDatabase.insertWeatherData(weather2);

        TestObserver<List<WeatherData>> observer = weatherDatabase.getAllWeatherData().test();
        observer.assertNoErrors();

        List<WeatherData> weathers = observer.values().get(0);
        DbTestUtils.assertWeather(weather1, weathers.get(0));
        DbTestUtils.assertWeather(weather2, weathers.get(1));
    }

    @Test
    public void testForecastReadWrite() {
        ForecastData forecast1 = DbTestUtils.createForecast1();
        weatherDatabase.insertForecastData(forecast1);

        TestObserver<ForecastData> observer = weatherDatabase.getForecastData(forecast1.placeId).test();
        observer.assertNoErrors();

        ForecastData result = observer.values().get(0);
        DbTestUtils.assertForecast(forecast1, result);
    }

    @Test
    public void testForecastReadWriteAll() {
        ForecastData forecast1 = DbTestUtils.createForecast1();
        ForecastData forecast2 = DbTestUtils.createForecast2();
        weatherDatabase.insertForecastData(forecast1);
        weatherDatabase.insertForecastData(forecast2);

        TestObserver<List<ForecastData>> observer = weatherDatabase.getAllForecastData().test();
        observer.assertNoErrors();

        List<ForecastData> forecasts = observer.values().get(0);
        DbTestUtils.assertForecast(forecast1, forecasts.get(0));
        DbTestUtils.assertForecast(forecast2, forecasts.get(1));
    }

    @After
    public void tearDown() {
        weatherDatabase.deleteAllPlaceData();
        weatherDatabase.deleteAllWeatherData();
        weatherDatabase.deleteAllForecastData();
    }
}
