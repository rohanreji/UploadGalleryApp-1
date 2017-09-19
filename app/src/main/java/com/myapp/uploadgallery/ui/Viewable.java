package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.Set;

import io.reactivex.Observable;

public interface Viewable {
    void showStubText();
    void showGallery(Set<GalleryImage> images);
    Observable<Void> showProgress(boolean show);
    Observable<Void> showNetworkAlert(Throwable throwable);
    Observable<Void> showUploadAlert(Throwable throwable);
}
