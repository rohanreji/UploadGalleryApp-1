package com.myapp.uploadgallery.ui;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.myapp.uploadgallery.MockGalleryApp;
import com.myapp.uploadgallery.manager.GalleryManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Inject
    GalleryManager manager;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MockGalleryApp app
                = (MockGalleryApp) instrumentation.getTargetContext().getApplicationContext();
    }


}