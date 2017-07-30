package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.maximpestryakov.yamblzweather.data.model.place.Place;

public class PreferencesStorage {
    public static final String KEY_PLACE_NAME = "place_name";
    public static final String KEY_PLACE_ID = "place_id";
    public static final String KEY_PLACE = "place";

    public static final String KEY_WEATHER_SCHEDULE_ENABLED = "weather_schedule_enabled";
    public static final String KEY_WEATHER_SCHEDULE_INTERVAL = "weather_schedule_interval";

    private SharedPreferences prefs;
    private Gson gson;

    public PreferencesStorage(Context context, Gson gson) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = gson;
    }

    public PreferencesStorage() {}

    public void setPlaceName(String placeName) {
        prefs.edit().putString(KEY_PLACE_NAME, placeName).apply();
    }

    public String getPlaceName() {
        return prefs.getString(KEY_PLACE_NAME, null);
    }

    public void setPlaceId(String placeId) {
        prefs.edit().putString(KEY_PLACE_ID, placeId).apply();
    }

    public String getPlaceId() {
        return prefs.getString(KEY_PLACE_ID, null);
    }

    public void setPlace(Place place) {
        prefs.edit().putString(KEY_PLACE, gson.toJson(place)).apply();
    }

    public Place getPlace() {
        if (!prefs.contains(KEY_PLACE)) {
            return null;
        }

        return gson.fromJson(prefs.getString(KEY_PLACE, null), Place.class);
    }

    public boolean isWeatherScheduleEnabled() {
        return prefs.getBoolean(PreferencesStorage.KEY_WEATHER_SCHEDULE_ENABLED, false);
    }

    public int getWeatherScheduleInterval() {
        return Integer.valueOf(prefs.getString("weather_schedule_interval", "30"));
    }
}
