
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    private float lat;
    @SerializedName("lng")
    private float lng;

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }
}
