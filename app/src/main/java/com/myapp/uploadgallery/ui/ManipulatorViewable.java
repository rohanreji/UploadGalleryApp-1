package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;
import android.net.Uri;

public interface ManipulatorViewable {
    void setBitmap(Bitmap bitmap, Uri toSave);
    void setManipulatorListener(ManipulatorListener listener);

    interface ManipulatorListener {
        void close();
        void onSaved();
        void onError();
        void showProgress();
        void onViewCreated();
    }
}