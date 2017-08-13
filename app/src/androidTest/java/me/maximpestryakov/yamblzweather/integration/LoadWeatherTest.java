package me.maximpestryakov.yamblzweather.integration;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import me.maximpestryakov.TestApp;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.api.PlacesApi;
import me.maximpestryakov.yamblzweather.data.api.WeatherApi;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherActivity;
import me.maximpestryakov.yamblzweather.util.DbTestUtils;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
// FIXME TEST DEPRECATED
public class LoadWeatherTest {
    private static final String TEST_PLACE_PREDICTION =
            "Paris Avenue, Earlwood, New South Wales, Australia";

    @Rule
    public ActivityTestRule<WeatherActivity> activityRule =
            new ActivityTestRule<>(WeatherActivity.class);

    @Inject
    Gson gson;
    @Inject
    WeatherDatabase weatherDatabase;
    @Inject
    PrefsRepository prefs;
    @Inject
    PlacesApi placesApi;
    @Inject
    WeatherApi weatherApi;

    private TestAppModule testAppModule;
    private PlaceData testPlace;

    @Before
    public void setUp() {
        TestApp app = TestUtils.getTestApp(getTargetContext());
        app.getTestAppComponent().inject(this);
        testAppModule = app.getTestAppModule();

        testPlace = DbTestUtils.createPlace1();
        testPlace.placeId = "ChIJD7fiBh9u5kcRYJSMaMOCCwQ";
        prefs.setPlaceId(testPlace.placeId);
        weatherDatabase.insertPlaceData(testPlace);

//        performDrawerNavigation(R.id.nav_weather);
    }

    @Test
    public void loadWeatherByPlaceSuccessTest() {
//        testAppModule.usePlacePredictionsSuccessMock();
//        testAppModule.usePlaceSuccessMock();
//        testAppModule.useWeatherSuccessMock();
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withText(testPlace.placeName)));
//
//        // Only replaceText() + closeSoftKeyboard() works for showing popup!
//        placeText.perform(replaceText("Paris"), closeSoftKeyboard());
//
//        onView(withText(TEST_PLACE_PREDICTION))
//                .inRoot(isPlatformPopup())
//                .perform(click());
//        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));
//
//        // Check screen content
//        ViewInteraction temperature = onView(withId(R.id.temperature));
//        temperature.check(matches(isDisplayed()));
//        String temperatureText = TestUtils.getString(activityRule, R.string.temperature, 293);
//        temperature.check(matches(withText(temperatureText)));
//
//        ViewInteraction time = onView(withId(R.id.time));
//        time.check(matches(isDisplayed()));
//        String timeText = TestUtils.getString(activityRule, R.string.time, "05:57:52");
//        time.check(matches(withText(timeText)));
    }

    @Test
    public void loadPlacePredictionsFailureTest() {
//        testAppModule.usePlacePredictionsFailureMock();
//        testAppModule.usePlaceSuccessMock();
//        testAppModule.useWeatherSuccessMock();
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withText(testPlace.placeName)));
//
//        // Only replaceText() + closeSoftKeyboard() works for showing popup!
//        placeText.perform(replaceText("Paris"), closeSoftKeyboard());
//
//        checkSnackbarIsDisplayed(R.string.error_place_predictions_api);
    }

    @Test
    public void loadPlaceFailureTest() {
//        testAppModule.usePlacePredictionsSuccessMock();
//        testAppModule.usePlaceFailureMock();
//        testAppModule.useWeatherSuccessMock();
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withText(testPlace.placeName)));
//
//        // Only replaceText() + closeSoftKeyboard() works for showing popup!
//        placeText.perform(replaceText("Paris"), closeSoftKeyboard());
//
//        onView(withText(TEST_PLACE_PREDICTION))
//                .inRoot(isPlatformPopup())
//                .perform(click());
//        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));
//
//        checkSnackbarIsDisplayed(R.string.error_place_api);
    }

    @Test
    public void loadWeatherFailureTest() {
//        testAppModule.usePlacePredictionsSuccessMock();
//        testAppModule.usePlaceSuccessMock();
//        testAppModule.useWeatherFailureMock();
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withText(testPlace.placeName)));
//
//        // Only replaceText() + closeSoftKeyboard() works for showing popup!
//        placeText.perform(replaceText("Paris"), closeSoftKeyboard());
//
//        onView(withText(TEST_PLACE_PREDICTION))
//                .inRoot(isPlatformPopup())
//                .perform(click());
//        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));
//
//        checkSnackbarIsDisplayed(R.string.error_weather_api);
    }

    @After
    public void tearDown() {
        prefs.setPlaceId(null);
        weatherDatabase.deletePlaceData(testPlace.placeId);
    }
}