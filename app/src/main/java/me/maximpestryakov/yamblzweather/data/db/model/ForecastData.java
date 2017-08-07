package me.maximpestryakov.yamblzweather.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;

@Entity(tableName = "forecast", indices = {@Index("place_id")})
public class ForecastData {
    @PrimaryKey
    @ColumnInfo(name = "place_id")
    public String placeId;
    @ColumnInfo(name = "forecast_timestamp")
    public long forecastTimestamp;
    @ColumnInfo(name = "forecast_data")
    public String forecastData;

    public ForecastData() {
    }

    @Ignore
    public ForecastData(String placeId) {
        this.placeId = placeId;
    }

    public List<ForecastItem> getParsedForecastData(Gson gson) {
        if (forecastData == null) {
            return new ArrayList<>(0);
        }

        Type listType = new TypeToken<ArrayList<ForecastItem>>() {
        }.getType();
        return gson.fromJson(forecastData, listType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForecastData that = (ForecastData) o;
        return placeId.equals(that.placeId);
    }

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }

    @Override
    public String toString() {
        return "ForecastData{" +
                "placeId='" + placeId + '\'' +
                ", forecastTimestamp=" + forecastTimestamp +
                ", forecastData='" + forecastData + '\'' +
                '}';
    }
}
