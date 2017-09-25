package com.myapp.uploadgallery;

import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.di.TestAppModule;

public class TestGalleryApp extends GalleryApp{
    @Override
    void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new TestAppModule(this))
                .build();
    }
}
