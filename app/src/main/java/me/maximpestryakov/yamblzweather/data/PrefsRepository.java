package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.Locale;

public class PrefsRepository {
    public static final String KEY_LANG = "lang";
    public static final String KEY_PLACE_ID = "place_id";

    public static final String KEY_WEATHER_SCHEDULE_ENABLED = "weather_schedule_enabled";
    public static final String KEY_WEATHER_SCHEDULE_INTERVAL = "weather_schedule_interval";

    private SharedPreferences prefs;
    private Gson gson;

    public PrefsRepository(Context context, Gson gson) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = gson;
    }

    public PrefsRepository() {}

    public void setLang(String lang) {
        prefs.edit().putString(KEY_LANG, lang).apply();
    }

    public String getLang() {
        return prefs.getString(KEY_LANG, Locale.getDefault().getLanguage());
    }

    public void setPlaceId(String placeId) {
        prefs.edit().putString(KEY_PLACE_ID, placeId).apply();
    }

    public String getPlaceId() {
        return prefs.getString(KEY_PLACE_ID, null);
    }

    public boolean isWeatherScheduleEnabled() {
        return prefs.getBoolean(PrefsRepository.KEY_WEATHER_SCHEDULE_ENABLED, false);
    }

    public int getWeatherScheduleInterval() {
        return Integer.valueOf(prefs.getString("weather_schedule_interval", "30"));
    }
}
