package me.maximpestryakov.yamblzweather.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("message")
    @Expose
    public float message;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("sunrise")
    @Expose
    public long sunrise;
    @SerializedName("sunset")
    @Expose
    public long sunset;
}
