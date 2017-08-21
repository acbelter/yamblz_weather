package me.maximpestryakov.yamblzweather.data.model.forecast;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.common.Clouds;
import me.maximpestryakov.yamblzweather.data.model.common.Main;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.common.Wind;

public class ForecastItem {
    @SerializedName("dt")
    @Expose
    public long dataTimestamp;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = new ArrayList<>();
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("dt_txt")
    @Expose
    public String dataTimestampStr;

    private String dateTag;
    private String timeTag;

    public void prepareTags() {
        // Example of string to split: "2017-01-30 11:00:00"
        if (dataTimestampStr != null) {
            String[] tags = dataTimestampStr.split(" ");
            dateTag = tags[0];
            timeTag = tags[1];
        }
    }

    public String getDateTag() {
        return dateTag;
    }

    public String getTimeTag() {
        return timeTag;
    }

    @Nullable
    public Weather getWeather() {
        return !weather.isEmpty() ? weather.get(0) : null;
    }
}
