package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.myapp.uploadgallery.model.UpImage;

import java.io.File;

import rx.Observable;

public interface GalleryManager {

    void onResume();
    Observable<File> onPictureChosen(Context context, Bitmap bitmap);
    Observable<UpImage> uploadCachedPicture(Context context, File file);

}
