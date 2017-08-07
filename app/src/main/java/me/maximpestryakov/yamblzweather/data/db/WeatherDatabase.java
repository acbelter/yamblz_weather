package me.maximpestryakov.yamblzweather.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.db.model.ForecastData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.db.model.WeatherData;

@Dao
public interface WeatherDatabase {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaceData(PlaceData placeData);

    @Query("SELECT * FROM place WHERE place_id = :placeId")
    Maybe<PlaceData> getPlaceData(String placeId);

    @Query("SELECT * FROM place")
    Single<List<PlaceData>> getAllPlaceData();

    @Query("DELETE FROM place WHERE place_id = :placeId")
    void deletePlaceData(String placeId);

    @Query("DELETE FROM place")
    void deleteAllPlaceData();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherData(WeatherData weatherData);

    @Query("SELECT * FROM weather WHERE place_id = :placeId")
    Maybe<WeatherData> getWeatherData(String placeId);

    @Query("SELECT * FROM weather")
    Single<List<WeatherData>> getAllWeatherData();

    @Query("DELETE FROM weather WHERE place_id = :placeId")
    void deleteWeatherData(String placeId);

    @Query("DELETE FROM weather")
    void deleteAllWeatherData();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecastData(ForecastData forecastData);

    @Query("SELECT * FROM forecast WHERE place_id = :placeId")
    Maybe<ForecastData> getForecastData(String placeId);

    @Query("SELECT * FROM forecast")
    Single<List<ForecastData>> getAllForecastData();

    @Query("DELETE FROM forecast WHERE place_id = :placeId")
    void deleteForecastData(String placeId);

    @Query("DELETE FROM forecast")
    void deleteAllForecastData();
}
