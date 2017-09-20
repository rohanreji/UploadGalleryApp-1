package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.Set;

public interface Viewable {
    void showStubText();
    void showGallery(Set<GalleryImage> images);
    void showProgress(boolean show);
    void showNetworkAlert(Throwable throwable);
    void showUploadAlert(Throwable throwable);
}
