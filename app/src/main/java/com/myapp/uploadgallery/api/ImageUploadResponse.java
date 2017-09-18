package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;

/**
 * Created by margarita on 9/18/17.
 */

public class ImageUploadResponse {
    @Expose
    String url;
    @Expose
    String createdAt;

    public String getUrl() {
        return url;
    }
    public void setUrl(final String url) {
        this.url = url;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }
}
