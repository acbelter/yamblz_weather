package me.maximpestryakov.yamblzweather.util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import me.maximpestryakov.TestApp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public final class TestUtils {
    private TestUtils() {
    }

    public static TestApp getTestApp(Context context) {
        return (TestApp) context.getApplicationContext();
    }

    public static String getString(ActivityTestRule rule,
                                   @StringRes int id,
                                   Object... formatArgs) {
        return rule.getActivity().getResources().getString(id, formatArgs);
    }

    public static ViewInteraction checkToolbarTitle(@StringRes int titleTextId) {
        return onView(allOf(isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(titleTextId)));
    }

    public static void checkViewWithTextIsDisplayed(@StringRes int textRes) {
        onView(withText(textRes)).check(matches(isDisplayed()));
    }

    public static void checkViewWithIdIsDisplayed(@IdRes int idRes) {
        onView(withId(idRes)).check(matches(isDisplayed()));
    }

    public static void checkSnackbarIsDisplayed(@StringRes int textRes) {
        try {
            // Delay for showing snackbar
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // Ignore
        }

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(textRes)))
                .check(matches(isDisplayed()));
    }
}
