
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.SerializedName;

public class PlaceResult {
    @SerializedName("result")
    private Place place;
    @SerializedName("status")
    private String status;

    public Place getPlace() {
        return place;
    }

    public String getStatus() {
        return status;
    }
}
