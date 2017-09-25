package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains list of user uploaded images.
 */
public class ImageResponse {
    @Expose
    ArrayList<GalleryImage> images;

    public ArrayList<GalleryImage> getImages() {
        return images;
    }

    public void setImages(List<GalleryImage> images) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.addAll(images);
    }

    public void setImages(GalleryImage... images) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.addAll(Arrays.asList(images));
    }
}
