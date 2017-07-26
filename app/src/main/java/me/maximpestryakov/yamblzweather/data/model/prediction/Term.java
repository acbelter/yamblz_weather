
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.SerializedName;

public class Term {
    @SerializedName("offset")
    private int offset;
    @SerializedName("value")
    private String value;

    public int getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }
}
