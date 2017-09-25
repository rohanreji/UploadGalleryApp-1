package com.myapp.uploadgallery;

import android.app.Activity;
import android.app.Application;

import com.myapp.uploadgallery.di.AppComponent;
import com.myapp.uploadgallery.di.AppModule;
import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.manager.UserId;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class GalleryApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    UserId userId;

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        appComponent.inject(this);

        JodaTimeAndroid.init(this);

        Picasso.with(this)
                .setLoggingEnabled(true);
    }

    void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    // TODO: 9/19/17 check internet connection on updateImages
}
