package me.maximpestryakov.yamblzweather.data.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    @Expose
    public float temp;
    @SerializedName("pressure")
    @Expose
    public float pressure;
    @SerializedName("humidity")
    @Expose
    public float humidity;
    @SerializedName("temp_min")
    @Expose
    public float tempMin;
    @SerializedName("temp_max")
    @Expose
    public float tempMax;
    @SerializedName("sea_level")
    @Expose
    public float seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public float groundLevel;
}
