package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.ui.Viewable;

/**
 * Manages calls between view and model.
 */
public interface GalleryManager {
    /**
     * Updates images for user.
     *
     * @return Single to simplify call chaining
     */
//    Single updateImages();

    void setView(Viewable view);

//    GalleryViewable.GalleryListener getGalleryListener();
//    ManipulatorViewable.ManipulatorListener getManipulatorListener();

    void loadImages();
    void uploadImage();
    void subscribe();
    void unsubscribe();
    void onDestroy();
}
