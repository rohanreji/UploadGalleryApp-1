package com.myapp.uploadgallery.di;

import com.google.gson.Gson;
import com.myapp.uploadgallery.GalleryApp;
import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.TestGalleryEndpoint;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {
    public TestAppModule(final GalleryApp application) {
        super(application);
    }

    @Override
    GalleryEndpoint provideGalleryEndpoint(final Gson gson) {
        return new TestGalleryEndpoint();
    }
}
