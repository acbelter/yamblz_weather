package me.maximpestryakov.yamblzweather.data.db.model;

public class FullWeatherData {
    protected WeatherData weather;
    protected ForecastData forecast;

    public FullWeatherData(WeatherData weather, ForecastData forecast) {
        this.weather = weather;
        this.forecast = forecast;
    }

    public WeatherData getWeather() {
        return weather;
    }

    public ForecastData getForecast() {
        return forecast;
    }
}
