package me.maximpestryakov.yamblzweather.data.model;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    private Main main;

    @SerializedName("dt")
    private long time;

    public Main getMain() {
        return main;
    }

    public long getTime() {
        return time;
    }

    public static class Main {

        @SerializedName("temp")
        private double temperature;

        public double getTemperature() {
            return temperature;
        }
    }
}
