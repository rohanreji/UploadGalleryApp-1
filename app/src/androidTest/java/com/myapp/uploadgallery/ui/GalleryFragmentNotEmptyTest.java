package com.myapp.uploadgallery.ui;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.manager.TestUserId;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(AndroidJUnit4.class)
public class GalleryFragmentNotEmptyTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(
                    MainActivity.class);

    @Before
    public void setup() {
        TestUserId.set("user2");
    }

    @Test
    public void testOnePicGallery() {
        TestUserId.set("user1");
        //verify gallery fragment is shown
        onView(withId(R.id.rvGallery)).check(matches(isDisplayed()));
        //verify gallery has 1 image
        onView(withId(R.id.rvGallery)).check(new RecyclerViewAssertion(1));
    }

    @Test
    public void testManyPicGallery() {
        TestUserId.set("user2");
        //verify gallery fragment is shown
        onView(withId(R.id.rvGallery)).check(matches(isDisplayed()));
        //verify gallery has 1 image
        onView(withId(R.id.rvGallery)).check(new RecyclerViewAssertion(greaterThan(1)));
    }

    //from here:https://github.com/googlesamples/android-testing/blob/master/ui/espresso
    // /IntentsAdvancedSample/app/src/androidTest/java/com/example/android/testing/espresso
    // /intents/AdvancedSample/ImageViewHasDrawableMatcher.java
    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }
}
