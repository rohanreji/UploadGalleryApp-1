package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;

import java.io.File;

public interface ManipulatorViewable {
    void setBitmapToManipulate(Bitmap bitmap);
    void setManipulatorListeners(ManipulatorListener listener, ManipulatorUiListener uilistener);

    interface ManipulatorListener {
        void onManipulatorCropped(File file, Bitmap bitmap);
    }

    interface ManipulatorUiListener {
        void closeManipulator();
        void onManipulationError();
    }
}
