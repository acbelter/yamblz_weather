
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlacesPredictionsResult {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("predictions")
    @Expose
    public List<Prediction> predictions = new ArrayList<>();

    public boolean success() {
        return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }
}
