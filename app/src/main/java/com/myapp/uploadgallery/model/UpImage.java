package com.myapp.uploadgallery.model;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Pojo for an image.
 */
public class UpImage {
    String location;
    int width, height;
    String uploadedLocation;
    long timestamp;

    public static final SimpleDateFormat inputFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public String getLocation() {
        return location;
    }
    public void setLocation(final String location) {
        this.location = location;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(final int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(final int height) {
        this.height = height;
    }
    public String getUploadedLocation() {
        return uploadedLocation;
    }
    public void setUploadedLocation(final String uploadedLocation) {
        this.uploadedLocation = uploadedLocation;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public CharSequence getFormattedTimestamp() {
        return DateUtils.getRelativeTimeSpanString(timestamp,
                Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS, 0);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpImage)) {
            return false;
        }

        final UpImage upImage = (UpImage) o;

        return timestamp == upImage.timestamp;

    }
    @Override
    public int hashCode() {
        return (int) (timestamp ^ (timestamp >>> 32));
    }
}
