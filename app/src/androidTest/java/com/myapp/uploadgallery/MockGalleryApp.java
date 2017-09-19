package com.myapp.uploadgallery;

public class MockGalleryApp extends GalleryApp {
    @Override
    void initComponent() {
        appComponent = DaggerMainActivityTest_TestComponent
                .builder()
                .build();
    }
}
