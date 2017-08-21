package me.maximpestryakov.yamblzweather.ui;

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
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherActivity;
import me.maximpestryakov.yamblzweather.util.DbTestUtils;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
// FIXME TEST DEPRECATED
public class WeatherUiTest {
    @Rule
    public ActivityTestRule<WeatherActivity> activityRule =
            new ActivityTestRule<>(WeatherActivity.class);

    @Inject
    Gson gson;
    @Inject
    WeatherDatabase weatherDatabase;
    @Inject
    PrefsRepository prefs;

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
    public void placeSelectionTest() {
//        testAppModule.usePlacePredictionsSuccessMock();
//        testAppModule.usePlaceSuccessMock();
//        testAppModule.useWeatherSuccessMock();
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withText("Moscow")));
//
//        // Only replaceText() + closeSoftKeyboard() works for showing popup!
//        placeText.perform(replaceText("Paris"), closeSoftKeyboard());
//
//        String selectedPlacePrediction = "Paris Avenue, Earlwood, New South Wales, Australia";
//        onView(withText(selectedPlacePrediction))
//                .inRoot(isPlatformPopup())
//                .perform(click());
//        placeText.check(matches(withText(selectedPlacePrediction)));
    }

    @After
    public void tearDown() {
        prefs.setPlaceId(null);
        weatherDatabase.deletePlaceData(testPlace.placeId);
    }
}
