package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import me.maximpestryakov.yamblzweather.data.model.common.Weather;

public class GeneralForecastItem {
    public final String dateTag;
    public final Weather weather;
    public final float temperature;
    public final long dataTimestamp;

    public GeneralForecastItem(String dateTag,
                               Weather weather,
                               float temperature,
                               long dataTimestamp) {
        this.dateTag = dateTag;
        this.weather = weather;
        this.temperature = temperature;
        this.dataTimestamp = dataTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralForecastItem that = (GeneralForecastItem) o;
        return dateTag.equals(that.dateTag);
    }

    @Override
    public int hashCode() {
        return dateTag.hashCode();
    }
}
