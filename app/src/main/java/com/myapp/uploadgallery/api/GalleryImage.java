package com.myapp.uploadgallery.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryImage implements Comparable<GalleryImage> {
    @Expose
    String url;

    @SerializedName("created_at")
    @Expose
    String createdAt;

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GalleryImage)) {
            return false;
        }

        final GalleryImage that = (GalleryImage) o;

        return createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return createdAt.hashCode();
    }

    @Override
    public int compareTo(@NonNull final GalleryImage galleryImage) {
        return createdAt.compareTo(galleryImage.createdAt);
    }
}
