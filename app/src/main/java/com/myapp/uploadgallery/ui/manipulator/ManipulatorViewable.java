package com.myapp.uploadgallery.ui.manipulator;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.myapp.uploadgallery.test.GalleryIdlingResource;

import java.io.File;

import io.reactivex.Single;

/**
 * Defines interface for manipulator view.
 */
public interface ManipulatorViewable {
    /**
     * Sets listeners for the manipulations.
     *
     * @param listener listener that is responsible for saving and uploading image
     */
    void setManipulatorListener(@Nullable final GalleryIdlingResource idlingResource,
                                ManipulatorListener listener);

    /**
     * Defines callback for manipulations listener.
     */
    interface ManipulatorListener {
        /**
         * Called when image is manipulated and ready to save and upload.
         *
         * @param cropOperation observable that performs crop operation (hot)
         */
        void onManipulatorCropped(@Nullable final GalleryIdlingResource idlingResource,
                                  File file, Single<Bitmap> cropOperation);

        void onManipulatorClosed();
    }
}
