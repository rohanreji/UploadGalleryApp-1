package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.util.UniqueList;

import rx.Observable;

/**
 * Created by margarita on 9/18/17.
 */

public interface Viewable {
    void showStubText();
    void showGallery(UniqueList<UpImage> images);
    Observable<Void> showProgress(boolean show);
    Observable<Void> showNetworkAlert(Throwable throwable);
}
