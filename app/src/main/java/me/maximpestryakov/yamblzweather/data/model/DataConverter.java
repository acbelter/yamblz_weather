package me.maximpestryakov.yamblzweather.data.model;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;

public class DataConverter {
    private static final double HPA_TO_MM_HG_FACTOR = 0.75006375541921d;
    private SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private Gson gson;

    public DataConverter(Gson gson) {
        this.gson = gson;
    }

    public PlaceData convert(PlaceResult result, String lang) {
        PlaceData data = new PlaceData(result.place.placeId);
        data.lat = result.place.geometry.location.lat;
        data.lng = result.place.geometry.location.lng;
        data.placeName = result.place.name;
        data.lang = lang;
        return data;
    }

    public WeatherData convert(WeatherResult result, String placeId) {
        WeatherData data = new WeatherData(placeId);
        data.weatherData = gson.toJson(result);
        return data;
    }

    public ForecastData convert(ForecastResult result, String placeId) {
        ForecastData data = new ForecastData(placeId);
        Type listType = new TypeToken<ArrayList<ForecastItem>>() {
        }.getType();
        data.forecastData = gson.toJson(result.forecast, listType);
        return data;
    }

    // See https://openweathermap.org/weather-conditions
    public @Nullable WeatherType getWeatherType(WeatherResult weather) {
        Weather w = weather.getWeather();
        if (w == null) {
            return null;
        }

        switch (w.icon) {
            case "01d":
                return WeatherType.SUN;
            case "01n":
                return WeatherType.NIGHT;
            case "02d":
                return WeatherType.CLOUDS;
            case "02n":
                return WeatherType.NIGHT_CLOUDS;
            case "03d":
                return WeatherType.CLOUDS;
            case "03n":
                return WeatherType.NIGHT_CLOUDS;
            case "04d":
                return WeatherType.CLOUDS;
            case "04n":
                return WeatherType.NIGHT_CLOUDS;
            case "09d":
                return WeatherType.RAIN;
            case "09n":
                return WeatherType.RAIN;
            case "10d":
                return WeatherType.RAIN;
            case "10n":
                return WeatherType.RAIN;
            case "11d":
                return WeatherType.STORM;
            case "11n":
                return WeatherType.STORM;
            case "13d":
                return WeatherType.SNOW;
            case "13n":
                return WeatherType.SNOW;
            case "50d":
                return WeatherType.SUN;
            case "50n":
                return WeatherType.NIGHT;
            default:
                return null;
        }
    }

    public int getTemperatureC(WeatherResult weather) {
        return (int) (getTemperatureK(weather) - 273.15f);
    }

    public int getTemperatureK(WeatherResult weather) {
        return (int) (weather.main.temp);
    }

    public int getCloudiness(WeatherResult weather) {
        if (weather.clouds == null) {
            return 0;
        }
        return weather.clouds.all;
    }

    public int getWind(WeatherResult weather) {
        if (weather.wind == null) {
            return 0;
        }
        return (int) weather.wind.speed;
    }

    public int getHumidity(WeatherResult weather) {
        return (int) weather.main.humidity;
    }

    public String getFormattedDayOfWeek(WeatherResult weather) {
        return weekDayFormat.format(getDate(weather.dataTimestamp));
    }

    public String getFormattedDate(WeatherResult weather) {
        return dateFormat.format(getDate(weather.dataTimestamp));
    }

//    public String getCurrentDate(List<ForecastItem> forecast) {
//        for (ForecastItem item : )
//    }

    private static Date getDate(long dataTimestamp) {
        return new Date(dataTimestamp * 1000);
    }
}
