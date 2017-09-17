package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.GalleryApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent {
    void inject(GalleryApp app);
}
