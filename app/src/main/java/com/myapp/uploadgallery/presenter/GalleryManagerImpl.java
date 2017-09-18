package com.myapp.uploadgallery.presenter;

import android.util.Log;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageRequest;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.ui.Viewable;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GalleryManagerImpl implements GalleryManager {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private final UniqueList<UpImage> images;
    private Viewable view;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
        images = new UniqueList<>();
    }

    @Override
    public void onResume() {
        endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse response) -> Observable.from(response.getImages()))
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> {
                    Log.d("IMAGE", "TERMINATED!");
                    if (images.size() == 0) {
                        view.showStubText();
                    } else {
                        view.showGallery(images);
                    }
                })
                .doOnNext((UpImage image) -> images.add(image))
                .subscribe(UpImage -> Log.d("Image", String.valueOf("image")));
    }

    public void setView(Viewable view) {
        this.view = view;
    }

    @Override
    public void onPictureChosen() {
        final ImageRequest imageRequest = new ImageRequest();
        endpoint.postImageForUser(imageRequest);
    }
}
