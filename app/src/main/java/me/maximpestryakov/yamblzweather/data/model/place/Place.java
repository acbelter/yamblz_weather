
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("geometry")
    @Expose
    public Geometry geometry;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("vicinity")
    @Expose
    public String vicinity;
}
