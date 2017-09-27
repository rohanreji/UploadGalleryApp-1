package com.myapp.uploadgallery.manager;

import android.net.Uri;

public interface SharedPreferencesHelper {
    void setImageUri(Uri uri);
    Uri getImageUri();
}
