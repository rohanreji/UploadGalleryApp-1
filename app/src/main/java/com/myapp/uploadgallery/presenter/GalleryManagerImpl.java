package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.api.ImageUploadResponse;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.model.UserId;
import com.myapp.uploadgallery.ui.Viewable;
import com.myapp.uploadgallery.util.UniqueList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;

public class GalleryManagerImpl implements GalleryManager {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private final UniqueList<UpImage> images;
    private Viewable view;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
        images = new UniqueList<>();
    }

    @Override
    public void onResume() {
        endpoint.getImagesForUser(userId.get())
                .flatMap((ImageResponse response) -> Observable.from(response.getImages()))
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> {
                    Log.d("IMAGE", "TERMINATED!");
                    if (images.size() == 0) {
                        view.showStubText();
                    } else {
                        view.showGallery(images);
                    }
                })
                .doOnNext((UpImage image) -> images.add(image))
                .subscribe(UpImage -> Log.d("Image", String.valueOf("image")));
    }

    public void setView(Viewable view) {
        this.view = view;
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
    public Observable<UpImage> uploadCachedPicture(final Context context, final File file) {
        return Observable.defer(() -> {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), body);

            return endpoint.postImageForUser(userId.get(), part)
                    .flatMap((ImageUploadResponse response) -> {
                        UpImage upImage = new UpImage();
                        upImage.setTimestamp(System.currentTimeMillis());
                        return Observable.just(upImage);
                    });
        });
    }
}
