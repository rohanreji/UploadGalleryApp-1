package com.myapp.uploadgallery.manager;

import android.content.Context;

import com.myapp.uploadgallery.api.TestGalleryEndpoint;
import com.myapp.uploadgallery.ui.Viewable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;

public class GalleryManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    TestGalleryEndpoint endpoint = new TestGalleryEndpoint();

    @Mock
    UserId userId;

    @Mock
    Viewable viewable;

    @Mock
    Context context;

    GalleryManager manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateImages() {
        Mockito.when(userId.get()).thenReturn("123");
        manager = new GalleryManagerImpl(userId, endpoint);
        manager.setView(viewable);

        TestObserver updateImagesObserver = new TestObserver();

        manager.updateImages().subscribeWith(updateImagesObserver);

        updateImagesObserver.awaitTerminalEvent(10, TimeUnit.SECONDS);

        updateImagesObserver.assertNoErrors();

        updateImagesObserver.assertSubscribed();
    }
}
