package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.ui.MainActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MockAppModule.class})
public interface MockAppComponent extends AppComponent {
    void inject(MainActivityTest mainActivityTest);
}
