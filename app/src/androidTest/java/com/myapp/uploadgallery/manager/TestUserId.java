package com.myapp.uploadgallery.manager;

import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Mocks user id for new user each run.
 */
public class TestUserId extends UserId {
    private String uuid;

    private static String test;

    public TestUserId(final SharedPreferences preferences) {
        super(preferences);
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String get() {
        return test != null ? test : uuid;
    }

    public static void set(String set) {
        test = set;
    }

    public static void reset() {
        test = null;
    }
}
