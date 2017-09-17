package com.myapp.uploadgallery.presenter;

import com.myapp.uploadgallery.model.UpImage;

import rx.Observable;

public interface MainPresenter {
    Observable<UpImage> checkImages(String userId);
}
