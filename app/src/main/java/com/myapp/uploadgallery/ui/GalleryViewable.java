package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.Set;

public interface GalleryViewable {
    void setImages(Set<GalleryImage> images);
    void setCallback(GalleryListener galleryListener);

    interface GalleryListener {
        void onViewCreated();
    }
}
