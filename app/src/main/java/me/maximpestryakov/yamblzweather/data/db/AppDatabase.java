package me.maximpestryakov.yamblzweather.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;

@Database(entities = {
        PlaceData.class,
        WeatherData.class,
        ForecastData.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherDatabase weatherDatabase();
}
