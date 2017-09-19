package com.myapp.uploadgallery.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.myapp.uploadgallery.MockGalleryApp;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class MockAppModule  {
    private final MockGalleryApp app;

    public MockAppModule(final MockGalleryApp application) {
        this.app = application;
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return Mockito.mock(SharedPreferences.class);
    }
}
