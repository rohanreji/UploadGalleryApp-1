package com.myapp.uploadgallery.api;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TestGalleryEndpoint implements GalleryEndpoint {
    @Override
    public Single<ImageResponse> getImagesForUser(@Query("user_id") final String userId) {
        final GalleryImage img1 = new GalleryImage("http://placehold.it/120x120&text=image1",
                "2017-09-18T15:14:20Z");
        switch (userId) {
            case "user1": {
                final ImageResponse item = new ImageResponse();
                item.setImages(img1);
                return Single.just(item);
            }
            default: {
                final ImageResponse item = new ImageResponse();
                item.setImages();
                return Single.just(item);
            }
        }
    }
    @Override
    public Single<GalleryImage> postImageForUser(@Path("userId") final String userId,
                                                 @Part final MultipartBody.Part image) {
        final GalleryImage img1 = new GalleryImage("http://placehold.it/120x120&text=image1",
                "2017-09-18T15:14:20Z");
        final GalleryImage img2 = new GalleryImage("http://placehold.it/120x120&text=image2",
                "2017-09-19T15:30:27Z");
        final GalleryImage img3 = new GalleryImage("http://placehold.it/120x120&text=image3",
                "2017-09-20T16:20:37Z");
        switch (userId) {
            case "user1": {
                return Single.just(img1);
            }
            case "user2": {
                return Single.just(img2);
            }
            default: {
                return Single.just(img3);
            }
        }
    }
}
