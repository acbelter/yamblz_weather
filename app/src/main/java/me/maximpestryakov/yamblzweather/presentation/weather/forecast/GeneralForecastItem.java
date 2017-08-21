package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;

public class GeneralForecastItem {
    public final String dateTag;
    public final String timeTag;
    public final Weather weather;
    public final float temperature;
    public final long dataTimestamp;
    public final ForecastItem item;

    public GeneralForecastItem(ForecastItem item) {
        this.dateTag = item.getDateTag();
        this.timeTag = item.getTimeTag();
        this.weather = item.getWeather();
        this.temperature = item.main.temp;
        this.dataTimestamp = item.dataTimestamp;
        this.item = item;
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
