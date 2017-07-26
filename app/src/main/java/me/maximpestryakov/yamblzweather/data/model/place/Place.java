
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("vicinity")
    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getVicinity() {
        return vicinity;
    }
}
