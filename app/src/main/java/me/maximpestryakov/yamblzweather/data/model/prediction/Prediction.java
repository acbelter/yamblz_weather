
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Prediction {
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("matched_substrings")
    @Expose
    public List<MatchedSubstring> matchedSubstrings = new ArrayList<>();
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("terms")
    @Expose
    public List<Term> terms = new ArrayList<>();

    @Override
    public String toString() {
        return description;
    }
}
