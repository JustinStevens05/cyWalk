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
    //@Rule
    //public ActivityScenarioRule<Goals> goals = new ActivityScenarioRule<>(Goals.class);

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

    @Test
    public void checkGoalSetting(){
        String goalTxt = "0.0/20";
        String setGoalAmount = "20";
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

        onView(withId(R.id.nav_goals)).perform(click());

        onView(withId(R.id.setGoalsBtn)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        //goals.getScenario().onActivity(activity -> {
        //    activity.newDaily.setText(setGoalAmount);
        //    activity.newWeekly.setText(setGoalAmount);
        //});
        onView(withId(R.id.new_weekly)).perform(typeText(setGoalAmount));
        onView(withId(R.id.new_daily)).perform(typeText(setGoalAmount));

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.dailySteps)).check(matches(withText(endsWith(goalTxt))));
        onView(withId(R.id.weeklySteps)).check(matches(withText(endsWith(goalTxt))));
    }

    @Test
    public void checkSendingFriendReq(){
        String endingTxt = "";
        String friendUsername = "jStevans";
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

        onView(withId(R.id.nav_social)).perform(click());
        onView(withId(R.id.friendsButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.entryUsername)).perform(typeText(friendUsername));
        onView(withId(R.id.friendSubmitBtn)).perform(click());

        onView(withId(R.id.entryUsername)).check(matches(withText(endsWith(endingTxt))));
    }

    @Test
    public void checkAcceptingFriendReq(){
        String username = "jStevans";
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

        onView(withId(R.id.nav_social)).perform(click());
        onView(withId(R.id.friendsButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.requestsTable)).check(matches(hasChildCount(1)));
    }
}

