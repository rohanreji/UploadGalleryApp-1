package com.myapp.uploadgallery.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.uploadgallery.BuildConfig;
import com.myapp.uploadgallery.GalleryApp;
import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.manager.GalleryManager;
import com.myapp.uploadgallery.manager.GalleryManagerImpl;
import com.myapp.uploadgallery.manager.UserId;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private static final String PREF_NAME = "GALLERY_USER_ID";
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
        return new UserId(application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE));
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
                .client(client())
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return adapter.create(GalleryEndpoint.class);
    }

    OkHttpClient client() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    @Singleton
    GalleryManager provideGalleryManager(GalleryEndpoint endpoint, UserId userId) {
        return new GalleryManagerImpl(userId, endpoint,
                Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
