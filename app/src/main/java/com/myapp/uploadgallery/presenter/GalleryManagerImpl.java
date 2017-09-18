package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.ui.MainViewable;

import rx.Observable;

public class GalleryManagerImpl implements GalleryManager {
    private UserId userId;
    private GalleryEndpoint endpoint;
    private MainViewable view;


    public GalleryManagerImpl(UserId userId, GalleryEndpoint endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
    }

    @Override
    public Observable<UpImage> checkImages() {
        return endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse response) -> Observable.from(response.getImages()));
    }

    @Override
    public boolean hasCamera(final Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void setView(final MainViewable view) {
        this.view = view;
    }


}
