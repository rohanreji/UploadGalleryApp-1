package com.myapp.uploadgallery.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import com.myapp.uploadgallery.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class FabClickTest {
    private Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
            hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubCameraIntent() {
        intending(not(isInternal())).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        Instrumentation.ActivityResult cameraResult = createImageCaptureActivityResultStub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(cameraResult);

        Instrumentation.ActivityResult galleryResult = createImageCaptureActivityResultStub();
        intending(expectedIntent).respondWith(galleryResult);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.READ_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CAMERA");
        }
    }

    private Instrumentation.ActivityResult createImageCaptureActivityResultStub() {
        Bundle bundle = new Bundle();

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));
        bundle.putParcelable(MainActivity.MANIPULATOR, imageUri);

        Intent resultData = new Intent();
        resultData.setData(imageUri);
        resultData.putExtras(bundle);

        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    @Test
    public void testFabDialogGalleryClick() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        //click on gallery
        onView(withText(R.string.dialog_upload_gallery)).perform(click());
        intended(expectedIntent);
        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorCancel)).check(matches(isDisplayed()));
        onView(withId(R.id.cropImageView)).check(matches(hasDrawable()));
    }

    @Test
    public void testFabDialogCameraClick() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        //click on gallery
        onView(withText(R.string.dialog_upload_camera)).perform(click());
        intended(expectedIntent);
        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorCancel)).check(matches(isDisplayed()));
        onView(withId(R.id.cropImageView)).check(matches(hasDrawable()));
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
