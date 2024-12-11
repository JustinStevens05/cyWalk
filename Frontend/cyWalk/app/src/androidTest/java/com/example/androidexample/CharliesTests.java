package com.example.androidexample;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class CharliesTests {

    private static final int SIMULATED_DELAY_MS = 2000;

    @Rule
    public ActivityScenarioRule<Login> login = new ActivityScenarioRule<>(Login.class);
//    @Rule
//    public ActivityScenarioRule<Dashboard> dashboard = new ActivityScenarioRule<>(Dashboard.class);

    @Test
    public void checkDistanceUpdate() {
        // Launch login activity and enter credentials
        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText("testUsername");
            activity.passwordEditText.setText("testPassword");
        });

        // Click the login button
        onView(withId(R.id.login_login_btn)).perform(click());

        // Ensure the dashboard activity is displayed
        onView(withId(R.id.dashboard)).check(matches(isDisplayed()));

        // Wait for UI updates (or use IdlingResource)
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Click the start route button
        onView(withId(R.id.btn_start_auto_route)).perform(click());

        // Wait for 3 seconds to simulate distance tracking
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        // Verify that the distance value is greater than 0
        onView(withId(R.id.txt_daily_distance))
                .check(matches(CustomMatchers.withValueGreaterThan(0)));
    }

    @Test
    public void checkProfileView() {
        String username = "testUsername";
        String password = "testPassword";

        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText(username);
            activity.passwordEditText.setText(password);
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.nav_profile)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.profile_txt_username)).check(matches(withText(endsWith(username))));
    }

    @Test
    public void checkSocialView() {
        String username = "testUsername";
        String password = "testPassword";

        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText(username);
            activity.passwordEditText.setText(password);
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.nav_social)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.title)).check(matches(withText(endsWith("LEADERBOARD"))));
    }

    @Test
    public void checkLogout() {
        String username = "testUsername";
        String password = "testPassword";

        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText(username);
            activity.passwordEditText.setText(password);
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.nav_profile)).perform(click());
        onView(withId(R.id.profile_btn_logout)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.login_username_edt)).check(matches(withText(endsWith(""))));
    }


    @Test
    public void checkLocationUpdate() {
        // Set up the login scenario
        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText("testUsername");
            activity.passwordEditText.setText("testPassword");
        });

        // Perform login
        onView(withId(R.id.login_login_btn)).perform(click());

        // Wait for simulated delay to ensure login is processed
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkImageUpload() {
        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText("testUsername");
            activity.passwordEditText.setText("testPassword");
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.nav_profile)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.txt_greeting)).check(matches(withText(endsWith("testUsername"))));
    }

    @Test
    public void checkAchievements() {
        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText("testUsername");
            activity.passwordEditText.setText("testPassword");
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.nav_profile))
                .perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS); // Wait for profile to load
        } catch (InterruptedException e) {}

        // Verify that the achievements ListView is populated
        onView(withId(R.id.lv_achievements)).check(matches(hasMinimumChildCount(1)));

    }
}

