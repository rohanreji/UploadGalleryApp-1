package com.myapp.uploadgallery.presenter;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.model.UserId;

import javax.inject.Inject;

import rx.Observable;

public class MainPresenterImpl implements MainPresenter {
    @Inject
    UserId userId;

    @Inject
    GalleryEndpoint endpoint;

    @Inject
    public MainPresenterImpl() {
    }

    @Override
    public Observable<UpImage> checkImages() {
        return endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse response) -> Observable.from(response.getImages()));
    }
}
