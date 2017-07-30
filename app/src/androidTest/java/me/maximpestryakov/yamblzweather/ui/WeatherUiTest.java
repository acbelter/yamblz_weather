package me.maximpestryakov.yamblzweather.ui;

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

@RunWith(AndroidJUnit4.class)
public class WeatherUiTest {
    @Rule
    public ActivityTestRule<NavigationActivity> activityRule =
            new ActivityTestRule<>(NavigationActivity.class);

    @Inject
    Gson gson;
    @Inject
    PreferencesStorage prefs;

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
    public void placeSelectionTest() {
        testAppModule.usePlacePredictionsSuccessMock();
        testAppModule.usePlaceSuccessMock();
        testAppModule.useWeatherSuccessMock();

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withText("Moscow")));

        // Only replaceText() + closeSoftKeyboard() works for showing popup!
        placeText.perform(replaceText("Paris"), closeSoftKeyboard());

        String selectedPlacePrediction = "Paris Avenue, Earlwood, New South Wales, Australia";
        onView(withText(selectedPlacePrediction))
                .inRoot(isPlatformPopup())
                .perform(click());
        placeText.check(matches(withText(selectedPlacePrediction)));
    }

    @After
    public void tearDown() throws Exception {
        prefs.setPlaceId(placeId);
        prefs.setPlace(place);
        prefs.setPlaceName(placeName);
    }
}
