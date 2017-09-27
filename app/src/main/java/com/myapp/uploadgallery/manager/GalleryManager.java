package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.ui.manipulator.ManipulatorViewable;

/**
 * Manages calls between view and model.
 */
public interface GalleryManager extends ManipulatorViewable.ManipulatorListener {
    void setView(Viewable view);
    void loadImages();
    void subscribe();
    void unsubscribe();
}
