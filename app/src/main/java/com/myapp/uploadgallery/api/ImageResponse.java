package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ImageResponse {
    @Expose
    ArrayList<ImageUploadResponse> images;

    public ArrayList<ImageUploadResponse> getImages() {
        return images;
    }
}
