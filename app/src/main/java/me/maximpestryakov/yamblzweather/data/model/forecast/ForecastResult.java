package me.maximpestryakov.yamblzweather.data.model.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.common.Result;

public class ForecastResult implements Result {
    @SerializedName("city")
    @Expose
    public City city;
    @SerializedName("cod")
    @Expose
    public String code;
    @SerializedName("message")
    @Expose
    public float message;
    @SerializedName("cnt")
    @Expose
    public int count;
    @SerializedName("list")
    @Expose
    public List<ForecastItem> forecast = new ArrayList<>();

    @Override
    public boolean success() {
        return "200".equals(code);
    }

    @Override
    public boolean fail() {
        return !success();
    }
}
