package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.maximpestryakov.yamblzweather.data.model.place.Place;

public class PreferencesStorage {
    private static final String KEY_PLACE_NAME = "place_name";
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_PLACE = "place";

    private SharedPreferences prefs;
    private Gson gson;

    public PreferencesStorage(Context context, Gson gson) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = gson;
    }

    public void savePlaceName(String placeName) {
        prefs.edit().putString(KEY_PLACE_NAME, placeName).apply();
    }

    public String loadPlaceName() {
        return prefs.getString(KEY_PLACE_NAME, null);
    }

    public void savePlaceId(String placeId) {
        prefs.edit().putString(KEY_PLACE_ID, placeId).apply();
    }

    public String loadPlaceId() {
        return prefs.getString(KEY_PLACE_ID, null);
    }

    public void savePlace(Place place) {
        prefs.edit().putString(KEY_PLACE, gson.toJson(place)).apply();
    }

    public Place loadPlace() {
        if (!prefs.contains(KEY_PLACE)) {
            return null;
        }

        return gson.fromJson(prefs.getString(KEY_PLACE, null), Place.class);
    }
}
