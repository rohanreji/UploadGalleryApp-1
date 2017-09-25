package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = GalleryFragmentModule.class)
    abstract MainActivity contributeGalleryFragmentInjector();
}


