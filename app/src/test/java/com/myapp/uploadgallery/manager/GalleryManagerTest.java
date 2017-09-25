package com.myapp.uploadgallery.manager;

import android.content.Context;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.Viewable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GalleryManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    GalleryEndpoint endpoint;

    @Mock
    UserId userId;

    @Mock
    Viewable view;

    @Mock
    Context context;

    GalleryManager manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadImagesSuccessful() {
        final String userId = "user1";

        final GalleryImage img1 = new GalleryImage("http://placehold.it/120x120&text=image1",
                "2017-09-18T15:14:20Z");
        final ImageResponse item = new ImageResponse();
        item.setImages(img1);
        when(endpoint.getImagesForUser(userId)).thenReturn(Single.just(item));

        when(this.userId.get()).thenReturn(userId);
        manager = new GalleryManagerImpl(this.userId, endpoint,
                Schedulers.trampoline(), Schedulers.trampoline());
        manager.setView(view);
        manager.loadImages();
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchImagesStarted();

        inOrder.verify(view, times(1)).onFetchImagesCompleted(item.getImages());
    }

    @Test
    public void testLoadEmptyImages() {
        final String userId = "user1";

        final ImageResponse item = new ImageResponse();
        item.setImages();
        when(endpoint.getImagesForUser(userId)).thenReturn(Single.just(item));

        when(this.userId.get()).thenReturn(userId);
        manager = new GalleryManagerImpl(this.userId, endpoint,
                Schedulers.trampoline(), Schedulers.trampoline());
        manager.setView(view);
        manager.loadImages();
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchImagesStarted();

        inOrder.verify(view, times(1)).onFetchImagesCompleted(item.getImages());
    }

    @Test
    public void testLoadImagesNotSuccessful() {
        final String userId = "user1";

        Exception exception = new Exception();
        when(endpoint.getImagesForUser(userId)).thenReturn(Single.error(exception));

        when(this.userId.get()).thenReturn(userId);
        manager = new GalleryManagerImpl(this.userId, endpoint,
                Schedulers.trampoline(), Schedulers.trampoline());
        manager.setView(view);
        manager.loadImages();
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchImagesStarted();

        inOrder.verify(view, times(1)).onFetchImagesError(exception);
    }

}
