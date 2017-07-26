
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.SerializedName;

public class MatchedSubstring {
    @SerializedName("length")
    private int length;
    @SerializedName("offset")
    private int offset;

    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }
}
