package com.myapp.uploadgallery.util;

import android.content.Context;

import com.myapp.uploadgallery.R;

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
        String prefix = context.getResources().getString(R.string.cached_image_prefix);
        String suffix = context.getResources().getString(R.string.cached_image_suffix);
        final File file = new File(context.getCacheDir(), prefix + suffix);
        if (!file.exists()) {
            try {
                File.createTempFile(prefix, suffix, context.getCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
