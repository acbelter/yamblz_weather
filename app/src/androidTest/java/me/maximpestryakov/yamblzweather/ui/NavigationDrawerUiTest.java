package me.maximpestryakov.yamblzweather.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import me.maximpestryakov.TestApp;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.db.WeatherDatabase;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.presentation.weather.WeatherActivity;
import me.maximpestryakov.yamblzweather.util.DbTestUtils;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
// FIXME TEST DEPRECATED
public class NavigationDrawerUiTest {
    @Rule
    public ActivityTestRule<WeatherActivity> activityRule =
            new ActivityTestRule<>(WeatherActivity.class);

    @Inject
    Gson gson;
    @Inject
    WeatherDatabase weatherDatabase;
    @Inject
    PrefsRepository prefs;

    private PlaceData testPlace;

    @Before
    public void setUp() {
        TestApp app = TestUtils.getTestApp(getTargetContext());
        app.getTestAppComponent().inject(this);

        testPlace = DbTestUtils.createPlace1();
        testPlace.placeId = "ChIJD7fiBh9u5kcRYJSMaMOCCwQ";
        prefs.setPlaceId(testPlace.placeId);
        weatherDatabase.insertPlaceData(testPlace);
    }

    @Test
    public void navigationDrawerOpenCloseTest() {
//        ViewInteraction drawerLayout = onView(withId(R.id.drawerLayout));
//        drawerLayout.perform(DrawerActions.open());
//        drawerLayout.check(matches(isOpen()));
//        drawerLayout.perform(DrawerActions.close());
//        drawerLayout.check(matches(isClosed()));
    }

//    @Test
//    public void showWeatherTest() {
//        // Show weather screen
////        performDrawerNavigation(R.id.nav_weather);
//        // Check screen title
//        checkToolbarTitle(R.string.nav_weather);
//        // Check screen content
//        ViewInteraction temperature = onView(withId(R.id.temperature));
//        temperature.check(matches(isDisplayed()));
//        String temperatureText = TestUtils.getString(activityRule, R.string.temperature, 25);
//        temperature.check(matches(withText(temperatureText)));
//
//        ViewInteraction time = onView(withId(R.id.time));
//        time.check(matches(isDisplayed()));
//        String timeText = TestUtils.getString(activityRule, R.string.time, "08:30:00");
//        time.check(matches(withText(timeText)));
//
//        ViewInteraction placeText = onView(withId(R.id.placeText));
//        placeText.check(matches(isDisplayed()));
//        placeText.check(matches(withHint(R.string.hint_find_place)));
//        placeText.check(matches(withText("Moscow")));
//    }
//
//    @Test
//    public void showSettingsTest() {
//        // Show settings screen
////        performDrawerNavigation(R.id.nav_settings);
//        // Check screen title
//        checkToolbarTitle(R.string.nav_settings);
//        // Check screen content
//        checkViewWithTextIsDisplayed(R.string.weather_settings);
//        checkViewWithTextIsDisplayed(R.string.update_on_schedule);
//        checkViewWithTextIsDisplayed(R.string.update_interval);
//        // FIXME Hack for finding CheckBoxPreference
//        onView(withClassName(is(AppCompatCheckBox.class.getName()))).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void showAboutTest() {
//        // Show about screen
////        performDrawerNavigation(R.id.nav_about);
//        // Check screen title
//        checkToolbarTitle(R.string.nav_about);
//        // Check screen content
//        checkViewWithIdIsDisplayed(R.id.about_text);
//    }

//    @After
//    public void tearDown() {
//        prefs.setPlaceId(null);
//        weatherDatabase.deletePlaceData(testPlace.placeId);
//    }
}
