package com.myapp.uploadgallery;

import android.app.Activity;
import android.app.Application;

import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.presenter.MainPresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class GalleryApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    MainPresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }
}
