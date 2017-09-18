package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;
import com.myapp.uploadgallery.model.GalleryImage;

import java.util.ArrayList;

public class ImageResponse {
    @Expose
    ArrayList<GalleryImage> images;

    public ArrayList<GalleryImage> getImages() {
        return images;
    }
}
