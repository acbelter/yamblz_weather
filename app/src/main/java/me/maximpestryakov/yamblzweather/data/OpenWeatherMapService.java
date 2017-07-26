package me.maximpestryakov.yamblzweather.data;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.model.weather.Weather;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    String URL = "http://api.openweathermap.org/data/2.5/";

    String APP_ID = "72c4739d285455a62f672a3a98a113ab";

    @GET("weather?units=metric&APPID=" + APP_ID)
    Single<Weather> getWeather(@Query("lat") float lat,
                               @Query("lon") float lng,
                               @Query("lang") String lang);
}
