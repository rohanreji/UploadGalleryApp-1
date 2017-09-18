package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ImageUploadResponse {
    @Expose
    String url;

    @SerializedName("created_at")
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
