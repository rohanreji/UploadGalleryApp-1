package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.myapp.uploadgallery.model.GalleryImage;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.Viewable;

import java.io.File;

import io.reactivex.Observable;

public interface GalleryManager {
    Observable onResume();
    Observable<File> onPictureChosen(Context context, Bitmap bitmap);
    Observable<GalleryImage> uploadCachedPicture(Context context, File file);
    void setView(Viewable view);
    GalleryViewable.GalleryListener getGalleryListener();
    Uri getPictureUri(Context context);
}
