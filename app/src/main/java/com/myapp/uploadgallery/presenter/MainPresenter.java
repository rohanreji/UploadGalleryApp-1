package com.myapp.uploadgallery.presenter;

import com.myapp.uploadgallery.model.UpImage;

import rx.Observable;

public interface MainPresenter extends BasePresenter {
    Observable<UpImage> checkImages();
}
