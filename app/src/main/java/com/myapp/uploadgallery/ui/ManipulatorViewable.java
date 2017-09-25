package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Defines interface for manipulator view.
 */
public interface ManipulatorViewable {
    /**
     * Sets bitmap to manipulate.
     *
     * @param bitmap loaded image to manipulate
     */
    void setBitmapToManipulate(Bitmap bitmap);

    /**
     * Sets listeners for the manipulations.
     *
     * @param listener   listener that is responsible for saving and uploading image
     * @param uilistener listener that is responsible for ui changes before and after manipulation
     */
    void setManipulatorListeners(ManipulatorListener listener, ManipulatorUiListener uilistener);

    /**
     * Defines callback for manipulations listener.
     */
    interface ManipulatorListener {
        /**
         * Called when image is manipulated and ready to save and upload.
         *
         * @param file   file to save image to
         * @param bitmap bitmap to save to file
         */
        void onManipulatorCropped(File file, Bitmap bitmap);
    }

    /**
     * Defines callback for manipulations ui listener.
     */
    interface ManipulatorUiListener {
        /**
         * Called after manipulations are finished.
         */
        void closeManipulator();

        /**
         * Called when manipulations are unsuccessful.
         */
        void onManipulationError();
    }
}
