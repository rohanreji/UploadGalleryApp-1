package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.util.UniqueList;

public interface GalleryViewable {
    void setImages(UniqueList<UpImage> images);
}
