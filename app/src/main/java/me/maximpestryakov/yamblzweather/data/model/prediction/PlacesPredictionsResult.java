
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlacesPredictionsResult {
    @SerializedName("status")
    private String status;
    @SerializedName("predictions")
    private List<Prediction> predictions = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }
}
