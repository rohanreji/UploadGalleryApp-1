package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.api.GalleryImage;

import java.util.List;
import java.util.Set;

/**
 * Defines interface for main view.
 */
public interface Viewable {
    /**
     * Shows text when no images are uploaded/found.
     */
    void showStubText();

    /**
     * Shows gallery of uploaded images.
     *
     * @param images images that were uploaded
     */
    void showGallery(Set<GalleryImage> images);

    /**
     * Shows/hides progressbar.
     *
     * @param show if true, progressbar is shown; if false, progressbar is hidden
     */
    void showProgress(boolean show);

    /**
     * Shows network alert in case of network/internet error.
     *
     * @param throwable throwable that contains error message
     */
    void showNetworkAlert(Throwable throwable);

    /**
     * Shows alert when upload was unsuccessful.
     *
     * @param throwable throwable that contains error message
     */
    void showUploadAlert(Throwable throwable);

    void onFetchImagesStarted();
    void onFetchImagesCompleted(List<GalleryImage> imageList);
    void onFetchImagesError(Throwable throwable);
}
