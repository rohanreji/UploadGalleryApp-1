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
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.manager.TestUserId;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class AlertUploadTest {
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
    public void testFabDialogGalleryOneImageSaved() {
        //click on fab
        onView(withId(R.id.fab)).perform(click());
        //verify dialog is visible
        onView(withText(R.string.dialog_upload_title)).check(matches(isDisplayed()));
        //click on gallery
        onView(withText(R.string.dialog_upload_gallery)).perform(click());
        //verify that manipulator fragment is shown
        onView(withId(R.id.ivManipulatorSave)).check(matches(isDisplayed()));
        //change to error responds
        TestUserId.set("error");
        //click on save
        onView(withId(R.id.ivManipulatorSave)).perform(click());

        //verify manipulator is hidden
        onView(withId(R.id.ivManipulatorCancel)).check(doesNotExist());
        //verify gallery fragment is not shown
        onView(withId(R.id.rvGallery)).check(doesNotExist());
        //verify network alert is shown
        onView(withText(R.string.dialog_manipulation_title)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_manipulation_message)).check(matches(isDisplayed()));
    }

}
