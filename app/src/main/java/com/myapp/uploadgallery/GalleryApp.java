package com.myapp.uploadgallery;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.myapp.uploadgallery.di.AppComponent;
import com.myapp.uploadgallery.di.AppModule;
import com.myapp.uploadgallery.di.DaggerAppComponent;
import com.myapp.uploadgallery.presenter.GalleryManager;
import com.myapp.uploadgallery.presenter.UserId;
import com.myapp.uploadgallery.ui.Viewable;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class GalleryApp extends Application implements HasActivityInjector,
        Application.ActivityLifecycleCallbacks {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    GalleryManager manager;

    @Inject
    UserId userId;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);

        registerActivityLifecycleCallbacks(this);

        JodaTimeAndroid.init(this);

        Picasso.with(this)
                .setLoggingEnabled(true);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
        if (activity instanceof Viewable) {
            manager.setView((Viewable) activity);
        }
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
