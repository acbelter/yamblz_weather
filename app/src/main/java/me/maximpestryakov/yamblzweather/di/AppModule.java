package me.maximpestryakov.yamblzweather.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.maximpestryakov.yamblzweather.data.PreferencesStorage;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.db.DatabaseHelper;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.NoInternetException;
import me.maximpestryakov.yamblzweather.util.StringUtil;
import nl.nl2312.rxcupboard2.RxCupboard;
import nl.nl2312.rxcupboard2.RxDatabase;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final Context applicationContext;

    public AppModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    Context provideApplicationContext() {
        return applicationContext;
    }

    @Provides
    PreferencesStorage providePreferences(Context context, Gson gson) {
        return new PreferencesStorage(context, gson);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(NetworkUtil networkUtil) {
        return new OkHttpClient.Builder()
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
    RxDatabase provideDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return RxCupboard.withDefault(db);
    }

    @Singleton
    @Provides
    NetworkUtil provideNetworkUtil(Context context) {
        return new NetworkUtil(context);
    }

    @Singleton
    @Provides
    StringUtil provideStringUtil(Context context) {
        return new StringUtil(context);
    }
}
