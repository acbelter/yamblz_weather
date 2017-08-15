package me.maximpestryakov.yamblzweather.presentation;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.WeatherType;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.presentation.weather.TimeTag;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.AverageWeather;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralForecastItem;

// TODO Refactor this class. Added selecting units for temperature and pressure.
public class DataFormatter {
    private static final float K_TO_C_COEFF = 273.15f;
    private static final double HPA_TO_MM_HG_FACTOR = 0.75006375541921d;

    private DateTimeFormatter dateTagFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
    private DateTimeFormatter weekDayFormat = DateTimeFormat.forPattern("EEEE");
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMM yyyy");
    private DateTimeFormatter shortDateFormat = DateTimeFormat.forPattern("EEE, dd MMM");
    private DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");

    public DateTime getDateTime(long dataTimestamp) {
        return new DateTime(dataTimestamp * 1000, DateTimeZone.UTC);
    }

    public int convertToC(float temperature) {
        return (int) fromKtoC(temperature);
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

        return getWeatherImageDrawableId(type);
    }

    @DrawableRes
    public int getWeatherImageDrawableId(WeatherType type) {
        switch (type) {
            case NIGHT:
                return R.drawable.night;
            case NIGHT_CLOUDS:
                return R.drawable.night_clouds;
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
            default:
                return 0;
        }
    }

    public WeatherType getWorstWeatherType(WeatherType... types) {
        if (types.length == 0 || types.length > 2) {
            return null;
        }

        if (types.length == 1) {
            return types[0];
        }

        WeatherType first = types[0];
        WeatherType second = types[1];

        if (first == null && second == null) {
            return null;
        } else if (first != null && second != null) {
            if (first.priority() > second.priority()) {
                return first;
            } else if (first.priority() < second.priority()) {
                return second;
            } else {
                return first;
            }
        } else if (first == null) {
            return second;
        } else {
            return first;
        }
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
        String str = weekDayFormat.print(getDateTime(weather.dataTimestamp));
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    public String getFormattedDate(WeatherResult weather) {
        return dateFormat.print(getDateTime(weather.dataTimestamp));
    }



    @DrawableRes
    public int getWeatherImageDrawableId(ForecastItem item) {
        return getWeatherImageDrawableId(item.getWeather());
    }

    public String getTime(ForecastItem item) {
        return timeFormat.print(getDateTime(item.dataTimestamp));
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
        // FIXME Small hack. Add one day a day when parsing the date at midnight
        DateTime dateTime = dateTagFormat.parseDateTime(item.dateTag);

        String str = shortDateFormat.print(dateTime);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    public int getTemperatureC(GeneralForecastItem item) {
        return (int) (fromKtoC(item.temperature));
    }

    public int getTemperatureK(GeneralForecastItem item) {
        return (int) item.temperature;
    }

    public AverageWeather[] getAverageWeathers(List<ForecastItem> forecast) {
        // result[0] - morning weather
        // result[1] - day weather
        // result[2] - evening weather
        // result[3] - night weather
        AverageWeather[] weathers = new AverageWeather[4];
        for (ForecastItem item : forecast) {
            switch (item.getTimeTag()) {
                // Morning
                case TimeTag.TIME_06:
                case TimeTag.TIME_09:
                    if (weathers[0] == null) {
                        weathers[0] = new AverageWeather();
                    }
                    appendItemToAverageWeather(weathers[0], item);
                    break;
                // Day
                case TimeTag.TIME_12:
                case TimeTag.TIME_15:
                    if (weathers[1] == null) {
                        weathers[1] = new AverageWeather();
                    }
                    appendItemToAverageWeather(weathers[1], item);
                    break;
                // Evening
                case TimeTag.TIME_18:
                case TimeTag.TIME_21:
                    if (weathers[2] == null) {
                        weathers[2] = new AverageWeather();
                    }
                    appendItemToAverageWeather(weathers[2], item);
                    break;
                // Night
                case TimeTag.TIME_00:
                case TimeTag.TIME_03:
                    if (weathers[3] == null) {
                        weathers[3] = new AverageWeather();
                    }
                    appendItemToAverageWeather(weathers[3], item);
                    // Fix weather type from sun to night
                    if (weathers[3].worstWeatherType != null) {
                        if (weathers[3].worstWeatherType == WeatherType.SUN) {
                            weathers[3].worstWeatherType = WeatherType.NIGHT;
                        } else if (weathers[3].worstWeatherType == WeatherType.CLOUDS) {
                            weathers[3].worstWeatherType = WeatherType.NIGHT_CLOUDS;
                        }
                    }
                    break;
            }
        }
        return weathers;
    }

    private void appendItemToAverageWeather(AverageWeather weather, ForecastItem item) {
            weather.temperaturesSum += item.main.temp;
            weather.temperaturesCount++;
            weather.worstWeatherType =
                    getWorstWeatherType(weather.worstWeatherType,
                            getWeatherType(item.getWeather()));
    }
}
