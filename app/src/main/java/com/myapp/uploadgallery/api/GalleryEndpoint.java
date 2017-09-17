package com.myapp.uploadgallery.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Completable;
import rx.Observable;

public interface GalleryEndpoint {

    @GET("images?user_id={userId}")
    Observable<ImageResponse> getImagesForUser(@Path("userId") String userId);

    @POST("images")
    Completable postImageForUser(@Body ImageRequest imageRequest);
}
