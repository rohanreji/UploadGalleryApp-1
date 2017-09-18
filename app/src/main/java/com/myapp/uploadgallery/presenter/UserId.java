package com.myapp.uploadgallery.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Unique identifier for each user install.
 */
public class UserId {
    private static final String PREF_NAME = "GALLERY_USER_ID";
    private static final String USER_ID = "GALLERY_USER_ID";

    private String userId;

    public UserId(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
