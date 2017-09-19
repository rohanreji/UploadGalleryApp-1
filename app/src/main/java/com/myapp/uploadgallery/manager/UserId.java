package com.myapp.uploadgallery.manager;

import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Unique identifier for each user install.
 */
public class UserId {
    private static final String USER_ID = "GALLERY_USER_ID";

    private String userId;

    public UserId(SharedPreferences preferences) {
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
