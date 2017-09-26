package com.myapp.uploadgallery.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class FabViewTest {
    @Rule
    public ActivityTestRule<com.myapp.uploadgallery.ui.MainActivity> mActivityRule = new ActivityTestRule<>(
            com.myapp.uploadgallery.ui.MainActivity.class);

    @Test
    public void testInitVisible() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        onView(withId(R.id.tvStub)).check(matches(isDisplayed()));
    }
}
