
package me.maximpestryakov.yamblzweather.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import me.maximpestryakov.yamblzweather.data.model.common.Result;

public class PlaceResult implements Result {
    @SerializedName("result")
    @Expose
    public Place place;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public boolean success() {
        return "OK".equals(status) && place != null;
    }

    @Override
    public boolean fail() {
        return !success();
    }
}
