package com.myapp.uploadgallery.manager;

import android.content.SharedPreferences;
import android.net.Uri;

public class SharedPreferencesHelperImpl implements SharedPreferencesHelper {
    private static final String URI = "URI";
    private SharedPreferences preferences;

    public SharedPreferencesHelperImpl(SharedPreferences preferences) {
        this.preferences = preferences;
    }
    @Override
    public void setImageUri(final Uri uri) {
        preferences.edit().putString(URI, String.valueOf(uri)).apply();
    }
    @Override
    public Uri getImageUri() {
        return Uri.parse(preferences.getString(URI, ""));
    }
}
