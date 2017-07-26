package me.maximpestryakov.yamblzweather.data;

import io.reactivex.Single;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesService {
    String URL = "https://maps.googleapis.com/maps/api/place/";

    String API_KEY = "AIzaSyCnD976NTKjdu4oE0y06yXRoIk9XnYJxw0";

    @GET("autocomplete/json?types=(cities)&key=" + API_KEY)
    Single<PlacesPredictionsResult> getPlacePredictions(@Query("input") String input,
                                                        @Query("language") String lang);

    @GET("details/json?&key=" + API_KEY)
    Single<PlaceResult> getPlaceData(@Query("placeid") String placeId,
                                     @Query("language") String lang);
}
