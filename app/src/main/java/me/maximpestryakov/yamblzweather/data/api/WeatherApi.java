package me.maximpestryakov.yamblzweather.data.api;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastResult;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    String APP_ID = "72c4739d285455a62f672a3a98a113ab";

    @GET("weather?APPID=" + APP_ID)
    Single<WeatherResult> getWeather(@Query("lat") float lat,
                                     @Query("lon") float lng,
                                     @Query("lang") String lang);

    @GET("forecast?APPID=" + APP_ID)
    Single<ForecastResult> getForecast(@Query("lat") float lat,
                                       @Query("lon") float lng,
                                       @Query("lang") String lang);
}
