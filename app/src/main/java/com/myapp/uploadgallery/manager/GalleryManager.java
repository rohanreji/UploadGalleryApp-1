package com.myapp.uploadgallery.manager;

import android.support.annotation.Nullable;

import com.myapp.uploadgallery.test.GalleryIdlingResource;
import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.ui.manipulator.ManipulatorViewable;

/**
 * Manages calls between view and model.
 */
public interface GalleryManager extends ManipulatorViewable.ManipulatorListener {
    void setView(Viewable view);
    void loadImages(@Nullable final GalleryIdlingResource idlingResource);
    void subscribe(@Nullable final GalleryIdlingResource idlingResource);
    void unsubscribe();
}
