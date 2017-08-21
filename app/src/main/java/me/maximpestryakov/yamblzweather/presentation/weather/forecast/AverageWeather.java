package me.maximpestryakov.yamblzweather.presentation.weather.forecast;


import me.maximpestryakov.yamblzweather.data.model.WeatherType;

// Describes the average weather in the morning, day, evening or night
public class AverageWeather {
    public float temperaturesSum;
    public int temperaturesCount;
    public WeatherType worstWeatherType;

    public float getAverageTemperature() {
        if (temperaturesCount == 0) {
            return 0;
        }
        return temperaturesSum / temperaturesCount;
    }
}
