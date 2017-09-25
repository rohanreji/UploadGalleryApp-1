package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.GalleryApp;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {
    public TestAppModule(final GalleryApp application) {
        super(application);
    }


}
