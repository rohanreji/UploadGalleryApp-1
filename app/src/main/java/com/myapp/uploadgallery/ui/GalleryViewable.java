package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.Set;

/**
 * Defines interface for gallery of uploaded images.
 */
public interface GalleryViewable {
    /**
     * Adds images to the gallery of uploaded images.
     *
     * @param images images to add
     */
    void setImages(Set<GalleryImage> images);

    /**
     * Sets callback for the gallery of uploaded images.
     *
     * @param galleryListener callback implementation
     */
    void setCallback(GalleryListener galleryListener);

    /**
     * Defines callback for the gallery of uploaded images.
     */
    interface GalleryListener {
        /**
         * Called to notify that view is created.
         */
        void onViewCreated();
    }
}
