
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceResult {
    @SerializedName("result")
    @Expose
    public Place place;
    @SerializedName("status")
    @Expose
    public String status;
}
