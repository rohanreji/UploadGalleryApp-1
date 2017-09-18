package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.model.GalleryImage;
import com.myapp.uploadgallery.util.UniqueList;

public interface GalleryViewable {
    void setImages(UniqueList<GalleryImage> images);
}
