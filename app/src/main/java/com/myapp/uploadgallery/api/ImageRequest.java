package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;

import java.io.File;

public class ImageRequest {
    @Expose
    String userId;

    @Expose
    File image;
    
    @Expose
    String createdAt;
}
