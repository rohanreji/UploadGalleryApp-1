package com.myapp.uploadgallery.ui.manipulator;

import android.graphics.Bitmap;

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
    void setManipulatorListener(ManipulatorListener listener);

    /**
     * Defines callback for manipulations listener.
     */
    interface ManipulatorListener {
        /**
         * Called when image is manipulated and ready to save and upload.
         *
         * @param cropOperation   observable that performs crop operation (hot)
         */
        void onManipulatorCropped(File file, Single<Bitmap> cropOperation);
    }
}
