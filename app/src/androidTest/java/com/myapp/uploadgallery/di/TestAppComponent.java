package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.GalleryApp;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class,
        AppModule.class, ActivityBuilder.class})
public interface TestAppComponent extends AndroidInjector<GalleryApp> {
    void inject(GalleryApp app);
}
