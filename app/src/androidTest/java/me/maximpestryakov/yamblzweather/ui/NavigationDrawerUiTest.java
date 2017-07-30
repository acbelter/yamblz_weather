package me.maximpestryakov.yamblzweather.ui;

import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatCheckBox;

import com.google.gson.Gson;

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
import me.maximpestryakov.yamblzweather.presentation.NavigationActivity;
import me.maximpestryakov.yamblzweather.util.ResReader;
import me.maximpestryakov.yamblzweather.util.TestUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static me.maximpestryakov.yamblzweather.util.TestUtils.checkToolbarTitle;
import static me.maximpestryakov.yamblzweather.util.TestUtils.checkViewWithIdIsDisplayed;
import static me.maximpestryakov.yamblzweather.util.TestUtils.checkViewWithTextIsDisplayed;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class NavigationDrawerUiTest {
    @Rule
    public ActivityTestRule<NavigationActivity> activityRule =
            new ActivityTestRule<>(NavigationActivity.class);

    @Inject
    Gson gson;
    @Inject
    PreferencesStorage prefs;

    @Before
    public void setUp() {
        TestApp app = TestUtils.getTestApp(getTargetContext());
        app.getTestAppComponent().inject(this);

        ResReader resReader = new ResReader();
        Place place = gson.fromJson(
                resReader.readString("json/place_data.json"), PlaceResult.class).getPlace();
        prefs.setPlaceId("12345");
        prefs.setPlace(place);
        prefs.setPlaceName("Moscow");
    }

    @Test
    public void navigationDrawerOpenCloseTest() {
        ViewInteraction drawerLayout = onView(withId(R.id.drawerLayout));
        drawerLayout.perform(DrawerActions.open());
        drawerLayout.check(matches(isOpen()));
        drawerLayout.perform(DrawerActions.close());
        drawerLayout.check(matches(isClosed()));
    }

    @Test
    public void showWeatherTest() {
        // Show weather screen
        performDrawerNavigation(R.id.nav_weather);
        // Check screen title
        checkToolbarTitle(R.string.nav_weather);
        // Check screen content
        ViewInteraction temperature = onView(withId(R.id.temperature));
        temperature.check(matches(isDisplayed()));
        String temperatureText = TestUtils.getString(activityRule, R.string.temperature, 25);
        temperature.check(matches(withText(temperatureText)));

        ViewInteraction time = onView(withId(R.id.time));
        time.check(matches(isDisplayed()));
        String timeText = TestUtils.getString(activityRule, R.string.time, "08:30:00");
        time.check(matches(withText(timeText)));

        ViewInteraction placeText = onView(withId(R.id.placeText));
        placeText.check(matches(isDisplayed()));
        placeText.check(matches(withHint(R.string.hint_city_name)));
        placeText.check(matches(withText("Moscow")));
    }

    @Test
    public void showSettingsTest() {
        // Show settings screen
        performDrawerNavigation(R.id.nav_settings);
        // Check screen title
        checkToolbarTitle(R.string.nav_settings);
        // Check screen content
        checkViewWithTextIsDisplayed(R.string.weather_settings);
        checkViewWithTextIsDisplayed(R.string.update_on_schedule);
        checkViewWithTextIsDisplayed(R.string.update_interval);
        // FIXME Hack for finding CheckBoxPreference
        onView(withClassName(is(AppCompatCheckBox.class.getName()))).check(matches(isDisplayed()));
    }

    @Test
    public void showAboutTest() {
        // Show about screen
        performDrawerNavigation(R.id.nav_about);
        // Check screen title
        checkToolbarTitle(R.string.nav_about);
        // Check screen content
        checkViewWithIdIsDisplayed(R.id.about_text);
    }

    private static void performDrawerNavigation(@IdRes int itemId) {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open());
        onView(withId(R.id.navigationView)).perform(
                NavigationViewActions.navigateTo(itemId));
    }
}
