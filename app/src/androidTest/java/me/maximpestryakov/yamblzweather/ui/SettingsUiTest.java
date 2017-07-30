package me.maximpestryakov.yamblzweather.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatCheckBox;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.PreferencesStorage;
import me.maximpestryakov.yamblzweather.presentation.NavigationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static me.maximpestryakov.yamblzweather.util.TestUtils.checkViewWithTextIsDisplayed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class SettingsUiTest {
    @Rule
    public ActivityTestRule<NavigationActivity> activityRule =
            new ActivityTestRule<>(NavigationActivity.class);

    private SharedPreferences prefs;
    private Boolean weatherScheduleEnabled;
    private String weatherScheduleInterval;

    @SuppressLint("ApplySharedPref")
    @Before
    public void setUp() {
        prefs = PreferenceManager.getDefaultSharedPreferences(activityRule.getActivity());
        if (prefs.contains(PreferencesStorage.KEY_WEATHER_SCHEDULE_ENABLED)) {
            weatherScheduleEnabled =
                    prefs.getBoolean(PreferencesStorage.KEY_WEATHER_SCHEDULE_ENABLED, false);
        }
        if (prefs.contains(PreferencesStorage.KEY_WEATHER_SCHEDULE_INTERVAL)) {
            weatherScheduleInterval =
                    prefs.getString(PreferencesStorage.KEY_WEATHER_SCHEDULE_INTERVAL, null);
        }
        prefs.edit()
                .remove(PreferencesStorage.KEY_WEATHER_SCHEDULE_ENABLED)
                .remove(PreferencesStorage.KEY_WEATHER_SCHEDULE_INTERVAL)
                .commit();

        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open());
        onView(withId(R.id.navigationView)).perform(
                NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    @Test
    public void updateScheduleUiTest() {
        // Hack for finding preference :(
        ViewInteraction checkBox = onView(withClassName(is(AppCompatCheckBox.class.getName())));
        // Verify checkbox disabled state
        checkBox.check(matches(isNotChecked()));
        onView(withText(R.string.update_interval)).check(matches(not(isEnabled())));
        // Verify checkbox enabled state
        checkBox.perform(click());
        checkBox.check(matches(isChecked()));
        onView(withText(R.string.update_interval)).check(matches(isEnabled()));
        // Open window for weather update interval selection
        onView(withText(R.string.update_interval)).perform(click());
        // Verify window ui
        checkViewWithTextIsDisplayed(R.string.update_interval);
        checkViewWithTextIsDisplayed(R.string.interval_30_minutes);
        checkViewWithTextIsDisplayed(R.string.interval_1_hour);
        checkViewWithTextIsDisplayed(R.string.interval_2_hours);
        checkViewWithTextIsDisplayed(R.string.interval_4_hours);
        checkViewWithTextIsDisplayed(R.string.interval_6_hours);
        checkViewWithTextIsDisplayed(R.string.interval_12_hours);
        checkViewWithTextIsDisplayed(R.string.interval_1_day);
        checkViewWithTextIsDisplayed(android.R.string.cancel);
        // Close window
        onView(withText(android.R.string.cancel)).perform(click());
        // Verify checkbox disabled state
        checkBox.perform(click());
        checkBox.check(matches(isNotChecked()));
        onView(withText(R.string.update_interval)).check(matches(not(isEnabled())));
    }

    @SuppressLint("ApplySharedPref")
    @After
    public void tearDown() throws Exception {
        if (weatherScheduleEnabled != null) {
            prefs.edit().putBoolean(
                    PreferencesStorage.KEY_WEATHER_SCHEDULE_ENABLED, weatherScheduleEnabled).commit();
        }
        if (weatherScheduleInterval != null) {
            prefs.edit().putString(
                    PreferencesStorage.KEY_WEATHER_SCHEDULE_INTERVAL, weatherScheduleInterval).commit();
        }
    }
}
