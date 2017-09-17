package com.myapp.uploadgallery.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }
}
