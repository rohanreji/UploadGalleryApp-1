package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;

import java.io.File;

public interface ManipulatorViewable {
    void setBitmap(Bitmap bitmap);
    void setManipulatorListeners(ManipulatorListener listener, ManipulatorUiListener uilistener);

    interface ManipulatorListener {
        void onCropped(File file, Bitmap bitmap);
    }

    interface ManipulatorUiListener {
        void close();
        void onError();
    }
}
