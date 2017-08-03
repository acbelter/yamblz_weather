
package me.maximpestryakov.yamblzweather.data.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    @Expose
    public float speed;
    @SerializedName("deg")
    @Expose
    public float degrees;
}
