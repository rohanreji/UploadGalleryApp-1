package com.myapp.uploadgallery.di;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.uploadgallery.BuildConfig;
import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.presenter.MainPresenter;
import com.myapp.uploadgallery.presenter.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @Singleton
    GalleryEndpoint provideCarelinkApiEndpoint(OkHttpClient client, Gson sparkGson) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(sparkGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return adapter.create(GalleryEndpoint.class);
    }

    @Provides
    @Singleton
    MainPresenter provideMainPresenter() {
        return new MainPresenterImpl();
    }
}
