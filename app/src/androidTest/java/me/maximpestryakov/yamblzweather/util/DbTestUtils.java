package me.maximpestryakov.yamblzweather.util;

import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;

import static org.assertj.core.api.Assertions.assertThat;

public class DbTestUtils {
    public static PlaceData createPlace1() {
        PlaceData data = new PlaceData("11111");
        data.lat = 111f;
        data.lng = 222f;
        data.placeName = "Moscow";
        data.lang = "ru";
        return data;
    }

    public static PlaceData createPlace2() {
        PlaceData data = new PlaceData("22222");
        data.lat = 333f;
        data.lng = 444f;
        data.placeName = "London";
        data.lang = "en";
        return data;
    }

    public static WeatherData createWeather1() {
        WeatherData data = new WeatherData("11111");
        data.weatherTimestamp = 100L;
        data.weatherData = "{data1}";
        return data;
    }

    public static WeatherData createWeather2() {
        WeatherData data = new WeatherData("22222");
        data.weatherTimestamp = 200L;
        data.weatherData = "{data2}";
        return data;
    }

    public static ForecastData createForecast1() {
        ForecastData data = new ForecastData("11111");
        data.forecastTimestamp = 100L;
        data.forecastData = "{data1}";
        return data;
    }

    public static ForecastData createForecast2() {
        ForecastData data = new ForecastData("22222");
        data.forecastTimestamp = 200L;
        data.forecastData = "{data2}";
        return data;
    }

    public static void assertPlace(PlaceData first, PlaceData second) {
        assertThat(first.placeId).isEqualTo(second.placeId);
        assertThat(first.lat).isEqualTo(second.lat);
        assertThat(first.lng).isEqualTo(second.lng);
        assertThat(first.placeName).isEqualTo(second.placeName);
        assertThat(first.lang).isEqualTo(second.lang);
    }

    public static void assertWeather(WeatherData first, WeatherData second) {
        assertThat(first.placeId).isEqualTo(second.placeId);
        assertThat(first.weatherTimestamp).isEqualTo(second.weatherTimestamp);
        assertThat(first.weatherData).isEqualTo(second.weatherData);
    }

    public static void assertForecast(ForecastData first, ForecastData second) {
        assertThat(first.placeId).isEqualTo(second.placeId);
        assertThat(first.forecastTimestamp).isEqualTo(second.forecastTimestamp);
        assertThat(first.forecastData).isEqualTo(second.forecastData);
    }
}
