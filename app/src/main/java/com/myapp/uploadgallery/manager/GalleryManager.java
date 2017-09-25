package com.myapp.uploadgallery.manager;

import android.graphics.Bitmap;

import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.ManipulatorViewable;
import com.myapp.uploadgallery.ui.Viewable;

import java.io.File;

import io.reactivex.Single;

public interface GalleryManager {
    Single updateImages();

    void setView(Viewable view);

    GalleryViewable.GalleryListener getGalleryListener();
    ManipulatorViewable.ManipulatorListener getManipulatorListener();

    Single<File> saveBitmap(final File file, final Bitmap bitmap);
}
