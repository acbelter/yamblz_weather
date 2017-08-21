package me.maximpestryakov.yamblzweather.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;

import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;

@Entity(tableName = "weather", indices = {@Index("place_id")})
public class WeatherData {
    @PrimaryKey
    @ColumnInfo(name = "place_id")
    public String placeId;
    @ColumnInfo(name = "weather_timestamp")
    public long weatherTimestamp;
    @ColumnInfo(name = "weather_data")
    public String weatherData;
    @Ignore
    public boolean fromCache;

    public WeatherData() {
    }

    @Ignore
    public WeatherData(String placeId) {
        this.placeId = placeId;
    }

    public WeatherResult getParsedWeatherData(Gson gson) {
        if (weatherData == null) {
            return null;
        }

        return gson.fromJson(weatherData, WeatherResult.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherData that = (WeatherData) o;
        return placeId.equals(that.placeId);
    }

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "placeId='" + placeId + '\'' +
                ", weatherTimestamp=" + weatherTimestamp +
                ", weatherData='" + weatherData + '\'' +
                '}';
    }
}
