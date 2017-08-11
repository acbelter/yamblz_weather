package me.maximpestryakov.yamblzweather.data.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;

public class DataConverter {
    private Gson gson;

    public DataConverter(Gson gson) {
        this.gson = gson;
    }

    public PlaceData convert(PlaceResult result, String lang) {
        PlaceData data = new PlaceData(result.place.placeId);
        data.lat = result.place.geometry.location.lat;
        data.lng = result.place.geometry.location.lng;
        data.placeName = result.place.name;
        data.lang = lang;
        return data;
    }

    public WeatherData convert(WeatherResult result, String placeId) {
        WeatherData data = new WeatherData(placeId);
        data.weatherData = gson.toJson(result);
        return data;
    }

    public ForecastData convert(ForecastResult result, String placeId) {
        ForecastData data = new ForecastData(placeId);
        Type listType = new TypeToken<ArrayList<ForecastItem>>() {
        }.getType();
        data.forecastData = gson.toJson(result.forecast, listType);
        return data;
    }
}
