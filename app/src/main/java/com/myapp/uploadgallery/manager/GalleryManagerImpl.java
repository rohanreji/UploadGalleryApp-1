package com.myapp.uploadgallery.manager;

import android.graphics.Bitmap;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.ManipulatorViewable;
import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.util.UniqueList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GalleryManagerImpl implements GalleryManager, GalleryViewable.GalleryListener,
        ManipulatorViewable.ManipulatorListener {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private final UniqueList<GalleryImage> images;
    private Viewable view;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
        images = new UniqueList<>();
    }

    @Override
    public void setView(Viewable view) {
        this.view = view;
    }

    @Override
    public Observable updateImages() {
        view.showProgress(true);
        return endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse imageResponse) -> Observable.fromIterable(
                        imageResponse.getImages()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext((GalleryImage iur) -> view.showProgress(true))
                .doOnNext((GalleryImage image) -> {
                    images.add(image);
                })
                .doOnError((Throwable t) -> view.showNetworkAlert(t))
                .doOnComplete(() -> {
                    imagesUpdated();
                });
    }

    private void imagesUpdated() {
        if (images.size() == 0) {
            view.showProgress(false);
            view.showStubText();
        } else {
            view.showGallery(images);
        }
    }

    @Override
    public Observable<File> saveBitmap(final File file, final Bitmap bitmap) {
        return Observable.defer(() -> {
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
            return Observable.just(file);
        });
    }

    @Override
    public Observable<GalleryImage> uploadCachedPicture(final File pictureFile) {
        return Observable.defer(() -> {
            RequestBody body =
                    RequestBody.create(MediaType.parse("image/*"), pictureFile);
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("image", pictureFile.getName(),
                            body);

            return endpoint.postImageForUser(userId.get(), part)
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((GalleryImage response) -> {
                        images.add(response);
                        imagesUpdated();
                        return Observable.empty();
                    });
        });
    }

    @Override
    public GalleryViewable.GalleryListener getGalleryListener() {
        return this;
    }

    @Override
    public void onViewCreated() {
        imagesUpdated();
        view.showProgress(false);
    }

    @Override
    public ManipulatorViewable.ManipulatorListener getManipulatorListener() {
        return this;
    }

    @Override
    public void onManipulatorCropped(final File pictureFile, final Bitmap bitmap) {
        saveBitmap(pictureFile, bitmap)
                .flatMap((File file) -> uploadCachedPicture(file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError((Throwable t) -> view.showUploadAlert(t))
                .doFinally(() -> view.showProgress(false))
                .subscribe();
    }
}
