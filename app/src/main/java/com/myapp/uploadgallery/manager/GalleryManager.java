package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.ui.manipulator.ManipulatorViewable;

/**
 * Manages calls between view and model.
 */
public abstract class GalleryManager implements ManipulatorViewable.ManipulatorListener {
    abstract public void setView(Viewable view);
    abstract public void loadImages();
    abstract public void subscribe();
    abstract public void unsubscribe();
    abstract public void onDestroy();
}
