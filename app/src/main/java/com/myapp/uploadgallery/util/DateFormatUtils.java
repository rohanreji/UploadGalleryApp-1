package com.myapp.uploadgallery.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by margarita on 9/18/17.
 */

public class DateFormatUtils {
    static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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
}
