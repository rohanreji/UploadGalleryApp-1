package com.myapp.uploadgallery.di;

import com.myapp.uploadgallery.GalleryApp;

import dagger.Module;

@Module
public class MockAppModule extends AppModule {

    public MockAppModule(final GalleryApp application) {
        super(application);
    }
//
//    @Override
//    Context provideContext() {
//        return Mockito.mock(Context.class);
//    }
//    @Override
//    GalleryEndpoint provideGalleryEndpoint(final Gson gson) {
//        return Mockito.mock(GalleryEndpoint.class);
//    }
//
//    @Override
//    UserId provideUserId() {
//        return Mockito.mock(UserId.class);
//    }
//
//    @Override
//    Gson provideGson() {
//        return Mockito.mock(Gson.class);
//    }
//
//    @Override
//    GalleryManager provideGalleryManager(final GalleryEndpoint endpoint, final UserId userId) {
//        return Mockito.mock(GalleryManager.class);
//    }
}
