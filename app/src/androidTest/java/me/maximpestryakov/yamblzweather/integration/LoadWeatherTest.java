package me.maximpestryakov.yamblzweather.integration;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
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
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.GooglePlacesService;
import me.maximpestryakov.yamblzweather.data.OpenWeatherMapService;
import me.maximpestryakov.yamblzweather.data.PreferencesStorage;
import me.maximpestryakov.yamblzweather.data.model.place.Place;
import me.maximpestryakov.yamblzweather.data.model.place.PlaceResult;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.presentation.NavigationActivity;
import me.maximpestryakov.yamblzweather.util.ResReader;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static me.maximpestryakov.yamblzweather.util.TestUtils.checkSnackbarIsDisplayed;

@RunWith(AndroidJUnit4.class)
public class LoadWeatherTest {
    private static final String TEST_PLACE_PREDICTION =
            "Paris Avenue, Earlwood, New South Wales, Australia";

    @Rule
    public ActivityTestRule<NavigationActivity> activityRule =
            new ActivityTestRule<>(NavigationActivity.class);

    @Inject
    Gson gson;
    @Inject
    PreferencesStorage prefs;
    @Inject
    GooglePlacesService placesApi;
    @Inject
    OpenWeatherMapService weatherApi;

    private String placeId;
    private Place place;
    private String placeName;

    private TestAppModule testAppModule;

    @Before
    public void setUp() {
        TestApp app = TestUtils.getTestApp(getTargetContext());
        app.getTestAppComponent().inject(this);
        testAppModule = app.getTestAppModule();

        placeId = prefs.getPlaceId();
        place = prefs.getPlace();
        placeName = prefs.getPlaceName();

        ResReader resReader = new ResReader();
        Place place = gson.fromJson(
                resReader.readString("json/place_data.json"), PlaceResult.class).getPlace();
        prefs.setPlaceId("12345");
        prefs.setPlace(place);
        prefs.setPlaceName("Moscow");

        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open());
        onView(withId(R.id.navigationView)).perform(
                NavigationViewActions.navigateTo(R.id.nav_weather));
    }

    @Test
    public void loadWeatherByPlaceSuccessTest() {
        testAppModule.usePlacePredictionsSuccessMock();
        testAppModule.usePlaceSuccessMock();
        testAppModule.useWeatherSuccessMock();

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withText("Moscow")));

        // Only replaceText() + closeSoftKeyboard() works for showing popup!
        placeText.perform(replaceText("Paris"), closeSoftKeyboard());

        onView(withText(TEST_PLACE_PREDICTION))
                .inRoot(isPlatformPopup())
                .perform(click());
        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));

        // Check screen content
        ViewInteraction temperature = onView(withId(R.id.temperature));
        temperature.check(matches(isDisplayed()));
        String temperatureText = TestUtils.getString(activityRule, R.string.temperature, 30);
        temperature.check(matches(withText(temperatureText)));

        ViewInteraction time = onView(withId(R.id.time));
        time.check(matches(isDisplayed()));
        String timeText = TestUtils.getString(activityRule, R.string.time, "05:57:52");
        time.check(matches(withText(timeText)));
    }

    @Test
    public void loadPlacePredictionsFailureTest() {
        testAppModule.usePlacePredictionsFailureMock();
        testAppModule.usePlaceSuccessMock();
        testAppModule.useWeatherSuccessMock();

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withText("Moscow")));

        // Only replaceText() + closeSoftKeyboard() works for showing popup!
        placeText.perform(replaceText("Paris"), closeSoftKeyboard());

        checkSnackbarIsDisplayed(R.string.error_place_predictions_api);
    }

    @Test
    public void loadPlaceFailureTest() {
        testAppModule.usePlacePredictionsSuccessMock();
        testAppModule.usePlaceFailureMock();
        testAppModule.useWeatherSuccessMock();

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withText("Moscow")));

        // Only replaceText() + closeSoftKeyboard() works for showing popup!
        placeText.perform(replaceText("Paris"), closeSoftKeyboard());

        onView(withText(TEST_PLACE_PREDICTION))
                .inRoot(isPlatformPopup())
                .perform(click());
        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));

        checkSnackbarIsDisplayed(R.string.error_place_api);
    }

    @Test
    public void loadWeatherFailureTest() {
        testAppModule.usePlacePredictionsSuccessMock();
        testAppModule.usePlaceSuccessMock();
        testAppModule.useWeatherFailureMock();

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withText("Moscow")));

        // Only replaceText() + closeSoftKeyboard() works for showing popup!
        placeText.perform(replaceText("Paris"), closeSoftKeyboard());

        onView(withText(TEST_PLACE_PREDICTION))
                .inRoot(isPlatformPopup())
                .perform(click());
        placeText.check(matches(withText(TEST_PLACE_PREDICTION)));

        checkSnackbarIsDisplayed(R.string.error_weather_api);
    }

    @After
    public void tearDown() {
        prefs.setPlaceId(placeId);
        prefs.setPlace(place);
        prefs.setPlaceName(placeName);
    }
}