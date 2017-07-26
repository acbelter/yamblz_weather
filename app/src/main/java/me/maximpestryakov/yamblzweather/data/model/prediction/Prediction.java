
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Prediction {
    @SerializedName("description")
    private String description;
    @SerializedName("matched_substrings")
    private List<MatchedSubstring> matchedSubstrings = new ArrayList<>();
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("terms")
    private List<Term> terms = null;

    public String getDescription() {
        return description;
    }

    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public List<Term> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return description;
    }
}
