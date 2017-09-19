package com.myapp.uploadgallery.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myapp.uploadgallery.util.DateFormatUtils;

public class GalleryImage {
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

    public long getCreatedAtTimestamp() {
        return DateFormatUtils.parseTime(createdAt);
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
}
