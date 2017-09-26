package com.myapp.uploadgallery.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FabViewTest {
    @Rule
    public ActivityTestRule<com.myapp.uploadgallery.ui.MainActivity> mActivityRule =
            new ActivityTestRule<>(
                    com.myapp.uploadgallery.ui.MainActivity.class);

    @Test
    public void testInitVisible() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        onView(withId(R.id.tvStub)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabDialog() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_upload_message)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_upload_camera)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_upload_gallery)).check(matches(isDisplayed()));
    }
}
