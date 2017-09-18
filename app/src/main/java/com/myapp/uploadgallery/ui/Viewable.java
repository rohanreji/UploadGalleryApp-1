package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.model.GalleryImage;
import com.myapp.uploadgallery.util.UniqueList;

import io.reactivex.Observable;

public interface Viewable {
    void showStubText();
    void showGallery(UniqueList<GalleryImage> images);
    Observable<Void> showProgress(boolean show);
    Observable<Void> showNetworkAlert(Throwable throwable);
}
