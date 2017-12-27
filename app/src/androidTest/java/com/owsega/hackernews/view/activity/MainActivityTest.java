package com.owsega.hackernews.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.owsega.hackernews.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkLists() {
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.list)).perform(scrollToPosition(1));
        onView(withId(R.id.list)).perform(click());
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(0, click()));
        getInstrumentation().waitForIdleSync();
        pressBack();
    }

    @Test
    public void checkFragmentAfterRotate() {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Fragment fragment = activityRule.getActivity().postFragment;
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.list)).check(matches(isDisplayed()));

        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.list)).check(matches(isDisplayed()));
//        Fragment fragment1 = activityRule.getActivity().postFragment;
    }

    @Test
    public void checkProgressBarAfterSwipeDown() {
        onView(withId(R.id.swipe_container)).perform(swipeDown());
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        activityRule.finishActivity();
    }
}
