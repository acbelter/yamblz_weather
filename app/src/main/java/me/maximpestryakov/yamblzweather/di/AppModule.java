package me.maximpestryakov.yamblzweather.di;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    HttpUrl originalUrl = originalRequest.url();

                    HttpUrl url = originalUrl.newBuilder()
                            .addQueryParameter("appid", OpenWeatherMapService.APP_ID)
                            .build();

                    Request request = originalRequest.newBuilder()
                            .url(url)
                            .build();

                    return chain.proceed(request);
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
    OpenWeatherMapService provideOpenWeatherMapService(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(OpenWeatherMapService.URL)
                .build()
                .create(OpenWeatherMapService.class);
    }
}
