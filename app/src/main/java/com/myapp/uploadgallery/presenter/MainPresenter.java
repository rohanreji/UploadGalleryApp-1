package com.myapp.uploadgallery.presenter;

import android.content.Context;

import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.ui.MainViewable;

import rx.Observable;

public interface MainPresenter extends BasePresenter {
    void setView(MainViewable view);

    Observable<UpImage> checkImages();

    boolean hasCamera(Context context);


}
