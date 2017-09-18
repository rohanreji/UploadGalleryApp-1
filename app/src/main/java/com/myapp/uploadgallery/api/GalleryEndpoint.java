package com.myapp.uploadgallery.api;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GalleryEndpoint {

    @GET("images")
    Observable<ImageResponse> getImagesForUser(@Query("user_id") String userId);

    @Multipart
    @POST("images/{userId}")
    Observable<ImageUploadResponse> postImageForUser(
            @Path("userId") String userId,
            @Part MultipartBody.Part image);
}