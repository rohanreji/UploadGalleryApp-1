package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.api.ImageUploadResponse;
import com.myapp.uploadgallery.model.GalleryImage;
import com.myapp.uploadgallery.ui.GalleryViewable;
import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.util.DateFormatUtils;
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

import static com.myapp.uploadgallery.util.DateFormatUtils.parseTime;

public class GalleryManagerImpl implements GalleryManager, GalleryViewable.GalleryViewableListener {
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
    public Observable onResume() {
        return endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse imageResponse) -> Observable.fromIterable(
                        imageResponse.getImages()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext((ImageUploadResponse image) -> {
                    GalleryImage galleryImage = new GalleryImage();
                    galleryImage.setCreated_at(DateFormatUtils.parseTime(image.getCreatedAt()));
                    galleryImage.setUrl(galleryImage.getUrl());
                    images.add(galleryImage);
                    view.showProgress(true);
                })
                .doOnError((Throwable t) -> view.showNetworkAlert(t))
                .doOnComplete(() -> imagesUpdated())
                .doFinally(() -> {
                    view.showProgress(false);
                    imagesUpdated();
                });
    }

    private void imagesUpdated() {
        if (images.size() == 0) {
            view.showStubText();
        } else {
            view.showGallery(images);
        }
    }

    @Override
    public Observable<File> onPictureChosen(Context context, Bitmap bitmap) {
        String name = "cached_bitmap.jpg";
        return Observable.defer(() -> {
            File file = new File(context.getCacheDir(), name);
            if (file.exists()) {
                file.delete();
            }
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
    public Observable<GalleryImage> uploadCachedPicture(final Context context, final File file) {
        return Observable.defer(() -> {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("image", file.getName(), body);

            return endpoint.postImageForUser(userId.get(), part)
                    .flatMap((ImageUploadResponse response) -> {
                        GalleryImage galleryImage = new GalleryImage();
                        galleryImage.setUrl(response.getUrl());
                        long time = parseTime(response.getCreatedAt());
                        galleryImage.setCreated_at(time);
                        return Observable.just(galleryImage);
                    });
        });
    }

    @Override
    public GalleryViewable.GalleryViewableListener getCallback() {
        return this;
    }

    @Override
    public void onViewCreated() {
        imagesUpdated();
    }
}
