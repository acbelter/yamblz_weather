package me.maximpestryakov.yamblzweather.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.common.Clouds;
import me.maximpestryakov.yamblzweather.data.model.common.Coord;
import me.maximpestryakov.yamblzweather.data.model.common.Main;
import me.maximpestryakov.yamblzweather.data.model.common.Result;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.data.model.common.Wind;

public class WeatherResult implements Result {
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = new ArrayList<>();
    @SerializedName("base")
    @Expose
    public String base;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public long dataTimestamp;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public int code;

    @Override
    public boolean success() {
        return code == 200 && !weather.isEmpty();
    }

    @Override
    public boolean fail() {
        return !success();
    }
}
