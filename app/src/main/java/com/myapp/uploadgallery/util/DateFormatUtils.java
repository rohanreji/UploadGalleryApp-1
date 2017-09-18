package com.myapp.uploadgallery.util;

import android.text.format.DateUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date format util methods.
 */
public class DateFormatUtils {
    static final SimpleDateFormat inputFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static long parseTime(String createdAt) {
        long time = System.currentTimeMillis();
        try {
            final Date parse = inputFormat.parse(createdAt, new ParsePosition(0));
            time = parse.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static CharSequence getFormattedTimestamp(long time) {
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();
        return DateUtils.getRelativeTimeSpanString(time, timeInMillis, DateUtils.MINUTE_IN_MILLIS,
                0);
    }
}
