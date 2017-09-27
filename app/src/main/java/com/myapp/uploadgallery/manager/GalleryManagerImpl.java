package com.myapp.uploadgallery.manager;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.test.GalleryIdlingResource;
import com.myapp.uploadgallery.ui.Viewable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GalleryManagerImpl implements GalleryManager {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private Viewable view;
    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;
    private CompositeDisposable subscriptions;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint,
                              Scheduler background, Scheduler main) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.backgroundScheduler = background;
        this.mainScheduler = main;
        subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(Viewable view) {
        this.view = view;
    }

    @Override
    public void loadImages(@Nullable final GalleryIdlingResource idlingResource) {
        view.onFetchImagesStarted();
        subscriptions.clear();

        Disposable disposable = endpoint.getImagesForUser(userId.get())
                .doOnSubscribe((Disposable disposable1) -> {
                    GalleryIdlingResource.set(idlingResource, true);
                })
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe((ImageResponse imageResponse) -> {
                    if (null != view) {
                        view.onFetchImagesCompleted(imageResponse.getImages());
                    }

                    GalleryIdlingResource.set(idlingResource, false);
                }, (Throwable throwable) -> {
                    if (null != view) {
                        view.onFetchImagesError(throwable);
                    }

                    GalleryIdlingResource.set(idlingResource, false);
                });
        subscriptions.add(disposable);
    }

    @Override
    public void subscribe(@Nullable final GalleryIdlingResource idlingResource) {
        loadImages(idlingResource);
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void onManipulatorClosed() {
        if (view != null) {
            view.onManipulatorClosed();
        }
    }

    @Override
    public void onManipulatorCropped(@Nullable final GalleryIdlingResource idlingResource,
                                     final File aFile, final Single<Bitmap> cropOperation) {
        subscriptions.clear();
        view.onUploadImageStarted();

        GalleryIdlingResource.set(idlingResource, true);
        Disposable disposable = cropOperation
                .flatMap((Bitmap bitmap) -> saveBitmap(aFile, bitmap))
                .flatMap((File file) -> uploadImage(file))
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe((GalleryImage imageResponse) -> {
                    if (null != view) {
                        view.onUploadImageCompleted(imageResponse);
                    }

                    GalleryIdlingResource.set(idlingResource, false);
                }, (Throwable throwable) -> {
                    if (null != view) {
                        view.onUploadImageError(throwable);
                    }


                    GalleryIdlingResource.set(idlingResource, false);
                });
        subscriptions.add(disposable);
    }

    private Single<File> saveBitmap(final File file, final Bitmap bitmap) {
        return Single.fromCallable(() -> {
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return file;
        });
    }

    private Single<GalleryImage> uploadImage(final File pictureFile) {
        RequestBody body =
                RequestBody.create(MediaType.parse("image/*"), pictureFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("image", pictureFile.getName(),
                        body);
        return endpoint.postImageForUser(userId.get(), part);
    }
}
