package com.myapp.uploadgallery.manager;

import android.graphics.Bitmap;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.ManipulatorViewable;
import com.myapp.uploadgallery.ui.Viewable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GalleryManagerImpl implements GalleryManager, GalleryViewable.GalleryListener,
        ManipulatorViewable.ManipulatorListener {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private final Set<GalleryImage> images;
    private Viewable view;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
        images = new TreeSet<GalleryImage>();
    }

    @Override
    public void setView(Viewable view) {
        this.view = view;
    }

    @Override
    public Single updateImages() {
        return endpoint.getImagesForUser(userId.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((Disposable disposable) -> view.showProgress(true))
                .doOnSuccess((ImageResponse imageResponse)
                        -> images.addAll(imageResponse.getImages()))
                .doOnError((Throwable throwable) -> view.showNetworkAlert(throwable))
                .doOnEvent((ImageResponse ir, Throwable t) -> imagesUpdated());
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
                    .doOnError((Throwable t) -> view.showUploadAlert(t))
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
                .subscribe(new Consumer<GalleryImage>() {
                    @Override
                    public void accept(final GalleryImage galleryImage) throws Exception {
                        view.showProgress(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        view.showUploadAlert(throwable);
                    }
                });
    }
}
