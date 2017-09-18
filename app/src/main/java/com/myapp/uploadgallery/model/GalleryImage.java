package com.myapp.uploadgallery.model;

/**
 * Pojo for an image.
 */
public class GalleryImage {
    private String url;
    private long created_at;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(final long created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GalleryImage)) {
            return false;
        }

        final GalleryImage galleryImage = (GalleryImage) o;

        return created_at == galleryImage.created_at;

    }
    @Override
    public int hashCode() {
        return (int) (created_at ^ (created_at >>> 32));
    }
}