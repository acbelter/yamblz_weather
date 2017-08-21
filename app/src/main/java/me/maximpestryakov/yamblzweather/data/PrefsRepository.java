package me.maximpestryakov.yamblzweather.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

public class PrefsRepository {
    public static final String KEY_LANG = "lang";
    public static final String KEY_PLACE_ID = "place_id";

    public static final String KEY_UPDATE_BY_SCHEDULE = "update_by_schedule";
    public static final String KEY_UPDATE_INTERVAL = "update_interval";

    private SharedPreferences prefs;

    public PrefsRepository(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
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

    public void setUpdateBySchedule(boolean value) {
        prefs.edit().putBoolean(KEY_UPDATE_BY_SCHEDULE, value).apply();
    }

    public boolean isUpdateBySchedule() {
        return prefs.getBoolean(KEY_UPDATE_BY_SCHEDULE, false);
    }

    public void setUpdateInterval(int interval) {
        prefs.edit().putInt(KEY_UPDATE_INTERVAL, interval).apply();
    }

    public int getUpdateInterval() {
        return prefs.getInt(KEY_UPDATE_INTERVAL, 0);
    }
}
