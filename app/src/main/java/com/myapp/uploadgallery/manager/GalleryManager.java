package com.myapp.uploadgallery.manager;

import android.graphics.Bitmap;

import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.ManipulatorViewable;
import com.myapp.uploadgallery.ui.Viewable;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface GalleryManager {
    Single updateImages();

    Observable<GalleryImage> uploadCachedPicture(File file);

    void setView(Viewable view);

    GalleryViewable.GalleryListener getGalleryListener();
    ManipulatorViewable.ManipulatorListener getManipulatorListener();

    Observable<File> saveBitmap(final File file, final Bitmap bitmap);
}
