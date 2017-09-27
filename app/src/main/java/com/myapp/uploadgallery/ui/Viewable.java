package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.List;

/**
 * Defines interface for main view.
 */
public interface Viewable {
    void onFetchImagesStarted();
    void onFetchImagesCompleted(List<GalleryImage> imageList);
    void onFetchImagesError(Throwable throwable);
    void onUploadImageError(Throwable throwable);
    void onUploadImageCompleted(GalleryImage image);
    void onUploadImageStarted();
    void onManipulatorClosed();
}
