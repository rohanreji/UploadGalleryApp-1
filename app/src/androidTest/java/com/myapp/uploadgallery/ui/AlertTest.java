package com.myapp.uploadgallery.ui;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.manager.TestUserId;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AlertTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(
                    MainActivity.class, false, false);

    private IdlingResource mIdlingResource;

    @Before
    public void setup() {
        TestUserId.set("error");
        mActivityRule.launchActivity(null);
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testGetImagesAlert() {
        //verify gallery fragment is not shown
        onView(withId(R.id.rvGallery)).check(doesNotExist());
        //verify network alert is shown
        onView(withId(R.string.dialog_network_title)).check(matches(isDisplayed()));
        onView(withId(R.string.dialog_network_message)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
