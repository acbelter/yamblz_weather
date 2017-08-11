package me.maximpestryakov.yamblzweather.presentation;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.WeatherType;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralForecastItem;

// TODO Refactor this class. Added selecting units for temperature and pressure.
public class DataFormatter {
    private static final float K_TO_C_COEFF = 273.15f;
    private static final double HPA_TO_MM_HG_FACTOR = 0.75006375541921d;

    private SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private Date getDate(long dataTimestamp) {
        return new Date(dataTimestamp * 1000);
    }

    private float fromKtoC(float temperature) {
        return temperature - K_TO_C_COEFF;
    }

    // See https://openweathermap.org/weather-conditions
    @Nullable
    public WeatherType getWeatherType(Weather weather) {
        if (weather == null) {
            return null;
        }

        switch (weather.icon) {
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

    @DrawableRes
    public int getWeatherImageDrawableId(Weather weather) {
        WeatherType type = getWeatherType(weather);
        if (type == null) {
            return 0;
        }

        switch (type) {
            case SUN:
                return R.drawable.sun;
            case CLOUDS:
                return R.drawable.clouds;
            case RAIN:
                return R.drawable.rain;
            case SNOW:
                return R.drawable.snow;
            case STORM:
                return R.drawable.storm;
            case NIGHT:
                return R.drawable.night;
            case NIGHT_CLOUDS:
                return R.drawable.night_clouds;
        }
        return 0;
    }



    @DrawableRes
    public int getWeatherImageDrawableId(WeatherResult weather) {
        return getWeatherImageDrawableId(weather.getWeather());
    }

    public int getTemperatureC(WeatherResult weather) {
        return (int) (fromKtoC(weather.main.temp));
    }

    public int getTemperatureK(WeatherResult weather) {
        return (int) weather.main.temp;
    }

    public int getCloudiness(WeatherResult weather) {
        return weather.clouds != null ? weather.clouds.all : 0;
    }

    public int getWind(WeatherResult weather) {
        return weather.wind != null ? (int) weather.wind.speed : 0;
    }

    public int getHumidity(WeatherResult weather) {
        return (int) weather.main.humidity;
    }

    public String getFormattedDayOfWeek(WeatherResult weather) {
        String str = weekDayFormat.format(getDate(weather.dataTimestamp));
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    public String getFormattedDate(WeatherResult weather) {
        return dateFormat.format(getDate(weather.dataTimestamp));
    }



    @DrawableRes
    public int getWeatherImageDrawableId(ForecastItem item) {
        return getWeatherImageDrawableId(item.getWeather());
    }

    public String getTime(ForecastItem item) {
        return timeFormat.format(getDate(item.dataTimestamp));
    }

    public int getTemperatureC(ForecastItem item) {
        return (int) (fromKtoC(item.main.temp));
    }

    public int getTemperatureK(ForecastItem item) {
        return (int) item.main.temp;
    }

    public int getCloudiness(ForecastItem item) {
        return item.clouds != null ? item.clouds.all : 0;
    }

    public int getWind(ForecastItem item) {
        return item.wind != null ? (int) item.wind.speed : 0;
    }

    public int getHumidity(ForecastItem item) {
        return (int) item.main.humidity;
    }

    public int getPressure(ForecastItem item) {
        return (int) ((double) item.main.pressure * HPA_TO_MM_HG_FACTOR);
    }



    @DrawableRes
    public int getWeatherImageDrawableId(GeneralForecastItem item) {
        return getWeatherImageDrawableId(item.weather);
    }

    public String getDate(GeneralForecastItem item) {
        return shortDateFormat.format(getDate(item.dataTimestamp));
    }

    public int getTemperatureC(GeneralForecastItem item) {
        return (int) (fromKtoC(item.temperature));
    }

    public int getTemperatureK(GeneralForecastItem item) {
        return (int) item.temperature;
    }
}
