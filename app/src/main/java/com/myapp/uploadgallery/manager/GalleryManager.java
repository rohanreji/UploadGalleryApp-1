package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.ManipulatorViewable;
import com.myapp.uploadgallery.ui.Viewable;

import io.reactivex.Single;

/**
 * Manages calls between view and model.
 */
public interface GalleryManager {
    /**
     * Updates images for user.
     *
     * @return Single to simplify call chaining
     */
    Single updateImages();

    void setView(Viewable view);

    GalleryViewable.GalleryListener getGalleryListener();
    ManipulatorViewable.ManipulatorListener getManipulatorListener();
}
