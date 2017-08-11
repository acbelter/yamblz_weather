package me.maximpestryakov.yamblzweather.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.maximpestryakov.yamblzweather.data.DataRepository;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.db.AppDatabase;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.model.DataConverter;
import me.maximpestryakov.yamblzweather.presentation.DataFormatter;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.NoInternetException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class AppModule {
    private final Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    @Provides
    Context provideApplicationContext() {
        return appContext;
    }

    @Provides
    PrefsRepository providePrefsRepository(Context context) {
        return new PrefsRepository(context);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(NetworkUtil networkUtil) {
        HttpLoggingInterceptor logging =
                new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    if (!networkUtil.isConnected()) {
                        throw new NoInternetException();
                    }
                    return chain.proceed(chain.request());
                })
                .build();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    WeatherApi provideOpenWeatherMapService(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(WeatherApi.BASE_URL)
                .build()
                .create(WeatherApi.class);
    }

    @Singleton
    @Provides
    PlacesApi provideGooglePlacesService(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(PlacesApi.BASE_URL)
                .build()
                .create(PlacesApi.class);
    }

    @Singleton
    @Provides
    DataConverter provideDataConverter(Gson gson) {
        return new DataConverter(gson);
    }

    @Singleton
    @Provides
    DataFormatter provideDataFormatter() {
        return new DataFormatter();
    }

    @Singleton
    @Provides
    WeatherDatabase provideDatabase(Context context) {
        AppDatabase db = Room.databaseBuilder(
                context,
                AppDatabase.class,
                "app_database")
                .build();
        return db.weatherDatabase();
    }

    @Singleton
    @Provides
    DataRepository provideDataRepository(WeatherDatabase database,
                                         PlacesApi placesApi,
                                         WeatherApi weatherApi,
                                         DataConverter dataConverter,
                                         Gson gson) {
        return new DataRepository(database, placesApi, weatherApi, dataConverter, gson);
    }

    @Singleton
    @Provides
    NetworkUtil provideNetworkUtil(Context context) {
        return new NetworkUtil(context);
    }
}
