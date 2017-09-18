package com.myapp.uploadgallery.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.uploadgallery.BuildConfig;
import com.myapp.uploadgallery.GalleryApp;
import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.presenter.GalleryManager;
import com.myapp.uploadgallery.presenter.GalleryManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private GalleryApp application;

    public AppModule(GalleryApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    UserId provideUserId() {
        return new UserId(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @Singleton
    GalleryEndpoint provideGalleryEndpoint(Gson gson) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return adapter.create(GalleryEndpoint.class);
    }

    @Provides
    @Singleton
    GalleryManager provideMainPresenter(GalleryEndpoint endpoint, UserId userId) {
        return new GalleryManagerImpl(userId, endpoint);
    }
}
