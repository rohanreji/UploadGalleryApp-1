package com.myapp.uploadgallery.di;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.myapp.uploadgallery.GalleryApp;
import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.TestGalleryEndpoint;
import com.myapp.uploadgallery.manager.TestUserId;
import com.myapp.uploadgallery.manager.UserId;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {
    public TestAppModule(final GalleryApp application) {
        super(application);
    }

    @Override
    GalleryEndpoint endpoint(final Gson gson) {
        return new TestGalleryEndpoint();
    }

    @NonNull
    @Override
    UserId userId() {
        return new TestUserId(getSharedPreferences());
    }
}
