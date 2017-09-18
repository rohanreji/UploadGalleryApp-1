package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.presenter.GalleryManager;

public interface MainViewable {
    void setManager(GalleryManager presenter);

    void startCamera();
    void startGallery();
}
