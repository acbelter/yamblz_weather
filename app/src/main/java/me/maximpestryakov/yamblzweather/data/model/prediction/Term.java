
package me.maximpestryakov.yamblzweather.data.model.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {
    @SerializedName("offset")
    @Expose
    public int offset;
    @SerializedName("value")
    @Expose
    public String value;
}
