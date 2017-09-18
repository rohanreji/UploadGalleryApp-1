package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.presenter.UniqueList;

/**
 * Created by margarita on 9/18/17.
 */

public interface Viewable {

    void showStubText();
    void showGallery(UniqueList<UpImage> images);
}
