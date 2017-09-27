package com.myapp.uploadgallery.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.Viewable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GalleryManagerSuccessfulTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    GalleryEndpoint endpoint;

    @Mock
    UserId userId;

    @Mock
    Viewable view;

    @Mock
    Context context;

    GalleryManager manager;

    private String userIdValue = "user1";
    private GalleryImage img1 = new GalleryImage("http://placehold.it/120x120&text=image1",
            "2017-09-18T15:14:20Z");
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.userId.get()).thenReturn(userIdValue);

        manager = new GalleryManagerImpl(this.userId, endpoint,
                Schedulers.trampoline(), Schedulers.trampoline());
        manager.setView(view);
    }

    @Test
    public void testLoadImagesSuccessful() {
        final ImageResponse item = new ImageResponse();
        item.setImages(img1);
        when(endpoint.getImagesForUser(userIdValue)).thenReturn(Single.just(item));

        manager.loadImages();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchImagesStarted();
        inOrder.verify(view, times(1)).onFetchImagesCompleted(item.getImages());
    }

    @Test
    public void testLoadEmptyImages() {
        final ImageResponse item = new ImageResponse();
        item.setImages();
        when(endpoint.getImagesForUser(userIdValue)).thenReturn(Single.just(item));

        manager.loadImages();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchImagesStarted();
        inOrder.verify(view, times(1)).onFetchImagesCompleted(item.getImages());
    }

    @Test
    public void testImageUpload() {
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        Mockito.when(
                bitmap.compress((Bitmap.CompressFormat) any(), Mockito.anyInt(),
                        (OutputStream) any())).
                thenReturn(true);

        File file;
        try {
            file = folder.newFile();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }

        when(endpoint.postImageForUser(eq(userIdValue), any())).thenReturn(Single.just(img1));
        manager.onManipulatorCropped(file, Single.just(bitmap));

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onUploadImageStarted();

        inOrder.verify(view, times(1)).onUploadImageCompleted(img1);
    }
}
