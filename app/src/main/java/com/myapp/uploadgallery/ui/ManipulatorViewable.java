package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;

public interface ManipulatorViewable {
    void setBitmap(Bitmap bitmap);
    void setManipulatorListener(ManipulatorListener listener);

    interface ManipulatorListener {
        void onViewCreated();
        void close();
        void save();
    }
}
