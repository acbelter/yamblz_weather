
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    @Expose
    public Location location;
}
