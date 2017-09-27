package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.ui.gallery.GalleryFragment;
import com.myapp.uploadgallery.ui.MainActivity;
import com.myapp.uploadgallery.ui.manipulator.ManipulatorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = GalleryFragmentModule.class)
    abstract GalleryFragment contributeGalleryFragmentInjector();

    @ContributesAndroidInjector(modules = ManipulatorFragmentModule.class)
    abstract ManipulatorFragment contributeManipulatorFragmentInjector();
}


