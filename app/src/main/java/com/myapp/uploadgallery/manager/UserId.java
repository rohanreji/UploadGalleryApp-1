package com.myapp.uploadgallery.manager;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Unique identifier for every app install.
 */
public class UserId {
    static final String USER_ID = "GALLERY_USER_ID";

    private String userId;

    public UserId(@NonNull SharedPreferences preferences) {
        this.userId = preferences.getString(USER_ID, null);
        if (null == userId) {
            this.userId = UUID.randomUUID().toString();
            preferences.edit().putString(USER_ID, this.userId).apply();
        }
    }

    public String get() {
        return userId;
    }
}
