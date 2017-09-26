package com.myapp.uploadgallery.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
public class FabViewTest {
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

    @Test
    public void testFabDialogGalleryClick() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        //click on gallery
        onView(withText(R.string.dialog_upload_gallery)).perform(click());
        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorSave)).check(matches(isDisplayed()));
    }

//    @Test
//    public void testFabDialogCameraClick() {
//        //click on fab
//        onView(withId(R.id.fab)).perform(click());
//        //verify dialog is visible
//        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
//        //click on gallery
//        onView(withText(R.string.dialog_upload_camera)).perform(click());
//        //verify that manipulator fragment is shown
//        onView(withId(R.id.ivManipulatorSave)).check(matches(isDisplayed()));
//    }
}
