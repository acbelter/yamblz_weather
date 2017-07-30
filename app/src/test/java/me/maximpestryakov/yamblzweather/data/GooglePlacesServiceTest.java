package me.maximpestryakov.yamblzweather.data;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import me.maximpestryakov.yamblzweather.data.model.place.Place;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.PlacesPredictionsResult;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;
import me.maximpestryakov.yamblzweather.di.DaggerTestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.util.NetworkUtil;
import me.maximpestryakov.yamblzweather.util.ResReader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

@RunWith(MockitoJUnitRunner.class)
public class GooglePlacesServiceTest {
    @Mock
    Context mockContext;
    @Inject
    GooglePlacesService placesApi;
    @Inject
    NetworkUtil networkUtil;
    private ResReader resReader;
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        TestAppModule testAppModule = new TestAppModule(mockContext);
        mockWebServer = testAppModule.getMockWebServer();
        resReader = new ResReader();

        TestAppComponent component = DaggerTestAppComponent.builder()
                .appModule(testAppModule)
                .build();
        component.inject(this);

        Mockito.when(networkUtil.isConnected()).thenReturn(true);
    }

    @Test
    public void testPlacePredictionsSuccessResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resReader.readString("json/place_predictions.json")));

        TestObserver<PlacesPredictionsResult> observer =
                placesApi.getPlacePredictions("Moscow", "en").test();
        observer.assertNoErrors();

        PlacesPredictionsResult result = observer.values().get(0);
        assertThat(result.getStatus()).isEqualTo("OK");
        assertThat(result.getPredictions()).isNotNull();
        assertThat(result.getPredictions().size()).isEqualTo(3);

        Prediction p0 = result.getPredictions().get(0);
        assertThat(p0.getDescription()).isEqualTo("Paris, France");
        assertThat(p0.getPlaceId()).isEqualTo("ChIJD7fiBh9u5kcRYJSMaMOCCwQ");

        Prediction p1 = result.getPredictions().get(1);
        assertThat(p1.getDescription()).isEqualTo("Paris Avenue, Earlwood, New South Wales, Australia");
        assertThat(p1.getPlaceId()).isEqualTo("ChIJrU3KAHG6EmsR5Uwfrk7azrI");

        Prediction p2 = result.getPredictions().get(2);
        assertThat(p2.getDescription()).isEqualTo("Paris Street, Carlton, New South Wales, Australia");
        assertThat(p2.getPlaceId()).isEqualTo("ChIJCfeffMi5EmsRp7ykjcnb3VY");
    }

    @Test
    public void testPlacePredictionsFailureResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        TestObserver<PlacesPredictionsResult> observer =
                placesApi.getPlacePredictions("Moscow", "en").test();
        observer.assertError(throwable -> true);
    }

    @Test
    public void testPlaceDataSuccessResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(resReader.readString("json/place_data.json")));

        TestObserver<PlaceResult> observer = placesApi.getPlaceData("123", "en").test();
        observer.assertNoErrors();

        PlaceResult result = observer.values().get(0);
        assertThat(result.getStatus()).isEqualTo("OK");
        assertThat(result.getPlace()).isNotNull();

        Place p = result.getPlace();
        assertThat(p.getGeometry()).isNotNull();
        assertThat(p.getGeometry().getLocation()).isNotNull();
        assertThat(p.getGeometry().getLocation().getLat()).isEqualTo(-33.8669710f, offset(1e-5f));
        assertThat(p.getGeometry().getLocation().getLng()).isEqualTo(151.1958750f, offset(1e-5f));
        assertThat(p.getPlaceId()).isEqualTo("ChIJN1t_tDeuEmsRUsoyG83frY4");
        assertThat(p.getVicinity()).isEqualTo("48 Pirrama Road, Pyrmont");
    }

    @Test
    public void testPlaceDataFailureResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        TestObserver<PlaceResult> observer = placesApi.getPlaceData("123", "en").test();
        observer.assertError(throwable -> true);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
