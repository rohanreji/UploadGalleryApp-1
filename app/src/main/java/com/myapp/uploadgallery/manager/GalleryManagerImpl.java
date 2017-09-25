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

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    private Single<File> saveBitmap(final File file, final Bitmap bitmap) {
        return Single.defer(() -> {
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
            return Single.just(file);
        });
    }

    private Single<GalleryImage> uploadCachedPicture(final File pictureFile) {
        RequestBody body =
                RequestBody.create(MediaType.parse("image/*"), pictureFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("image", pictureFile.getName(),
                        body);

        return endpoint.postImageForUser(userId.get(), part)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError((Throwable t) -> view.showUploadAlert(t))
                .doOnSuccess((GalleryImage image) -> {
                    images.add(image);
                    imagesUpdated();
                })
                .doOnEvent((GalleryImage ir, Throwable t) -> imagesUpdated());
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
                .subscribe((GalleryImage galleryImage) -> view.showProgress(false),
                        (Throwable throwable) -> view.showUploadAlert(throwable));
    }
}
