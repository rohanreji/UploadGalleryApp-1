package com.myapp.uploadgallery.util;

import android.content.Context;

import java.io.File;

/**
 * File-related methods used throughout app.
 */
public class FileUtils {
    /**
     * Returns path to saved file in cache.
     *
     * @param context application context
     * @return file with path to cache directory
     */
    public static File getPictureFile(final Context context) {
        String name = "cached_bitmap.jpg";
        final File file = new File(context.getCacheDir(), name);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
