package com.myapp.uploadgallery.ui;

import com.myapp.uploadgallery.presenter.MainPresenter;

public interface MainViewable {
    void setPresenter(MainPresenter presenter);

    void startCamera();
    void startGallery();
}
