package com.myapp.uploadgallery.presenter;

import android.content.Context;

import com.myapp.uploadgallery.model.UpImage;

import rx.Observable;

public interface MainPresenter extends BasePresenter {
    Observable<UpImage> checkImages();

    boolean hasCamera(Context context);
}
