package com.myapp.uploadgallery;

import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.di.MockAppModule;

public class MockGalleryApp extends GalleryApp {
    @Override
    void initComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new MockAppModule(this))
                .build();
    }
}
