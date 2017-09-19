package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.util.UniqueList;

public interface GalleryViewable {
    void setImages(UniqueList<GalleryImage> images);
    void setCallback(GalleryListener galleryListener);

    interface GalleryListener {
        void onViewCreated();
    }
}
