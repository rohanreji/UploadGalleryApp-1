package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;
import com.myapp.uploadgallery.model.UpImage;

import java.util.ArrayList;

public class ImageResponse {
    @Expose
    ArrayList<UpImage> images;

    public ArrayList<UpImage> getImages() {
        return images;
    }
}
