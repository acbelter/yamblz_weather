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

    @Nullable
    public Weather getWeather() {
        return !weather.isEmpty() ? weather.get(0) : null;
    }
}
