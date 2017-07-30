package me.maximpestryakov.yamblzweather.di;

import android.content.Context;

import com.google.gson.Gson;

import org.mockito.Mockito;

import me.maximpestryakov.yamblzweather.data.GooglePlacesService;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestAppModule extends AppModule {
    private final MockWebServer mockWebServer;

    public TestAppModule(Context applicationContext) {
        super(applicationContext);
        mockWebServer = new MockWebServer();
    }

    public MockWebServer getMockWebServer() {
        return mockWebServer;
    }

    @Override
    OpenWeatherMapService provideOpenWeatherMapService(OkHttpClient client,
                                                       Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(mockWebServer.url("/"))
                .build()
                .create(OpenWeatherMapService.class);
    }

    @Override
    GooglePlacesService provideGooglePlacesService(OkHttpClient client,
                                                   Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(mockWebServer.url("/"))
                .build()
                .create(GooglePlacesService.class);
    }

    @Override
    NetworkUtil provideNetworkUtil(Context context) {
        return Mockito.mock(NetworkUtil.class);
    }
}
