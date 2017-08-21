package me.maximpestryakov.yamblzweather.data.db.model;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;

public class FullWeatherData {
    private boolean fromCache;
    private String placeId;
    private long weatherTimestamp;
    private WeatherResult weather;
    private long forecastTimestamp;
    private List<ForecastItem> forecast;

    public FullWeatherData(Gson gson,
                           WeatherData weatherData,
                           ForecastData forecastData) {
        fromCache = weatherData.fromCache || forecastData.fromCache;
        placeId = weatherData.placeId;
        weatherTimestamp = weatherData.weatherTimestamp;
        weather = weatherData.getParsedWeatherData(gson);
        forecastTimestamp = forecastData.forecastTimestamp;
        forecast = forecastData.getParsedForecastData(gson);

        for (ForecastItem item : forecast) {
            item.prepareTags();
        }

        Collections.sort(forecast, (item1, item2) -> {
            if (item1.dataTimestamp < item2.dataTimestamp) {
                return -1;
            }
            if (item1.dataTimestamp > item2.dataTimestamp) {
                return 1;
            }
            return 0;
        });
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public String getPlaceId() {
        return placeId;
    }

    public long getWeatherTimestamp() {
        return weatherTimestamp;
    }

    public WeatherResult getWeather() {
        return weather;
    }

    public long getForecastTimestamp() {
        return forecastTimestamp;
    }

    public List<ForecastItem> getForecast() {
        return forecast;
    }
}
