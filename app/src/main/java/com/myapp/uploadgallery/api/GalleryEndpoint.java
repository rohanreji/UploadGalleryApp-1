package com.myapp.uploadgallery.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public interface GalleryEndpoint {

    @GET("images")
    Observable<ImageResponse> getImagesForUser(@Query("user_id") String userId);

    @POST("images")
    Completable postImageForUser(@Body ImageRequest imageRequest);
}
