package com.myapp.uploadgallery;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.presenter.MainPresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class GalleryApp extends Application implements HasActivityInjector,
        Application.ActivityLifecycleCallbacks {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    MainPresenter presenter;

    @Inject
    UserId userId;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public void onActivityCreated(final Activity activity, final Bundle bundle) {

    }

    @Override
    public void onActivityStarted(final Activity activity) {

    }
    @Override
    public void onActivityResumed(final Activity activity) {

    }
    @Override
    public void onActivityPaused(final Activity activity) {

    }
    @Override
    public void onActivityStopped(final Activity activity) {

    }
    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {

    }
    @Override
    public void onActivityDestroyed(final Activity activity) {

    }
}
