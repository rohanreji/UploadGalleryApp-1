package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.net.Uri;

import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.Viewable;

import io.reactivex.Observable;

public interface GalleryManager {
    Observable onResume();
    Observable<GalleryImage> uploadCachedPicture(Context context);
    void setView(Viewable view);
    GalleryViewable.GalleryListener getGalleryListener();
    Uri getPictureUri(Context context);
}
