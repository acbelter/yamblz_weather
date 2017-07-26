package me.maximpestryakov.yamblzweather.data.model.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    private Main main;
    @SerializedName("dt")
    private long time;
    @SerializedName("name")
    private String name;

    public Main getMain() {
        return main;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public static class Main {

        @SerializedName("temp")
        private double temperature;

        public double getTemperature() {
            return temperature;
        }
    }
}
