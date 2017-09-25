package com.myapp.uploadgallery.util;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Calendar;

/**
 * Date format methods used throughout app.
 */
public class DateFormatUtils {
    /**
     * Parses string timestamp returned from server into long timestamp used for sorting.
     *
     * @param createdAt iso8601-formatted string from server
     * @return timestamp in millis
     */
    public static long parseTime(@NonNull String createdAt) {
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        final DateTime dateTime = parser2.parseDateTime(createdAt);
        return dateTime.getMillis();
    }

    /**
     * Formats timestamp in millis into relative time span format.
     *
     * @param time timestamp in millis
     * @return string in relative time span format
     */
    public static CharSequence getFormattedTimestamp(long time) {
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();
        return DateUtils.getRelativeTimeSpanString(time, timeInMillis, DateUtils.MINUTE_IN_MILLIS,
                0);
    }
}
