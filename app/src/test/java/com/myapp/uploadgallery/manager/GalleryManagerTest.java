package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.Viewable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyString;

/**
 * Created by margarita on 9/19/17.
 */

public class GalleryManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    GalleryEndpoint endpoint;

    @Mock
    UserId userId;

    @Mock
    Viewable viewable;

    GalleryManager manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateImages() {
        manager = new GalleryManagerImpl(userId, endpoint);
        manager.setView(viewable);
        ImageResponse imageResponse = new ImageResponse();
        GalleryImage image = new GalleryImage();
        image.setUrl("some url");
        image.setCreatedAt("some date");
        imageResponse.setImages(Arrays.asList(image));

        TestObserver updateImagesObserver = new TestObserver();

        Mockito.when(endpoint.getImagesForUser(anyString())).thenReturn(Observable.just(imageResponse));
        manager.updateImages().subscribeWith(updateImagesObserver);

        updateImagesObserver.awaitTerminalEvent(10, TimeUnit.SECONDS);

        updateImagesObserver.assertNoErrors();

        updateImagesObserver.assertSubscribed();
    }
}
