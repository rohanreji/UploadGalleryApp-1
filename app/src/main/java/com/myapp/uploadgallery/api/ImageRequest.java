package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;

public class ImageRequest {
    @Expose
    String userId;

    @Expose
    String image;
    
    @Expose
    String createdAt;
}
