package com.myapp.uploadgallery.model;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Pojo for an image.
 */
public class UpImage {
    String url;
    long created_at;

    public static final SimpleDateFormat inputFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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

    public CharSequence getFormattedTimestamp() {
        return DateUtils.getRelativeTimeSpanString(created_at,
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

        return created_at == upImage.created_at;

    }
    @Override
    public int hashCode() {
        return (int) (created_at ^ (created_at >>> 32));
    }
}
