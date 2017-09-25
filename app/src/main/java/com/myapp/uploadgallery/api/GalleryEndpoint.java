package com.myapp.uploadgallery.api;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API Endpoint definition.
 */
public interface GalleryEndpoint {
    /**
     * Get list of images uploaded by user.
     *
     * @param userId user id
     * @return {@link Single}<{@link ImageResponse}>
     */
    @GET("images")
    Single<ImageResponse> getImagesForUser(@Query("user_id") String userId);

    /**
     * Uploads image for user.
     *
     * @param userId user id
     * @param image  image to upload
     * @return {@link Single}<{@link GalleryImage}>
     */
    @Multipart
    @POST("images/{userId}")
    Single<GalleryImage> postImageForUser(
            @Path("userId") String userId,
            @Part MultipartBody.Part image);
}