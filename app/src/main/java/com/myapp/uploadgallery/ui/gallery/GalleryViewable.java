package com.myapp.uploadgallery.ui.gallery;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.List;

/**
 * Defines interface for gallery of uploaded images.
 */
public interface GalleryViewable {
    /**
     * Adds images to the gallery of uploaded images.
     *
     * @param images images to add
     */
    void setImages(List<GalleryImage> images);

    void addImage(GalleryImage image);
}
