package com.example.androidexample;

import com.example.androidexample.Login;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class JustinsTests {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<Login> login = new ActivityScenarioRule<>(Login.class);
    public ActivityScenarioRule<Dashboard> dashboard = new ActivityScenarioRule<>(Dashboard.class);

    /**
     * Start the server and run this test
     *
     * check that logging in a user is successful
     */
    @Test
    public void checkLogin(){
        String username = "jStevens";
        String password = "123";

        login.getScenario().onActivity(activity -> {
            activity.usernameEditText.setText(username);
            activity.passwordEditText.setText(password);
        });

        onView(withId(R.id.login_login_btn)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.txt_greeting)).check(matches(withText(endsWith(username))));
    }
}

