package com.myapp.uploadgallery.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * Created by margarita on 9/19/17.
 */

public class FileUtils {
    public static Uri getPictureUri(Context context) {
        File file = getPictureFile(context);
        return Uri.fromFile(file);
    }

    public static File getPictureFile(final Context context) {
        String name = "cached_bitmap.jpg";
        final File file = new File(context.getCacheDir(), name);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
