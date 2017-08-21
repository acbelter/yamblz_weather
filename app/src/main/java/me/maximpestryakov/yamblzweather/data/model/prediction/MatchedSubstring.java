
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchedSubstring {
    @SerializedName("length")
    @Expose
    public int length;
    @SerializedName("offset")
    @Expose
    public int offset;
}
