package me.maximpestryakov.yamblzweather.data.model;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    private Main main;

    public Main getMain() {
        return main;
    }

    public static class Main {

        @SerializedName("temp")
        private double temp;

        public double getTemp() {
            return temp;
        }
    }
}
