package com.myapp.uploadgallery.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ImageAddingTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubCameraIntent() {
        Instrumentation.ActivityResult cameraResult = createImageCaptureActivityResultStub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(cameraResult);

        Instrumentation.ActivityResult galleryResult = createImageCaptureActivityResultStub();
        intending(hasAction(Intent.ACTION_PICK)).respondWith(galleryResult);

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
        bundle.putParcelable(MainActivity.MANIPULATOR, BitmapFactory.decodeResource(
                mIntentsRule.getActivity().getResources(), R.drawable.ic_image));

        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    @Test
    public void testFabClickCamera() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));

        //click on camera button
        onView(withText(R.string.dialog_upload_camera)).perform(click());

        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorSave)).check(matches(isDisplayed()));

        //click cancel
        onView(withId(R.id.ivManipulatorCancel)).perform(click());
    }

    @Test
    public void testFabClickGallery() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));

        //click on camera button
        onView(withText(R.string.dialog_upload_gallery)).perform(click());

        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorSave)).check(matches(isDisplayed()));

        //click cancel
        onView(withId(R.id.ivManipulatorCancel)).perform(click());
    }
}
