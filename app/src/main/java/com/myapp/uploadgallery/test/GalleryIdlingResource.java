package com.myapp.uploadgallery.test;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class GalleryIdlingResource implements IdlingResource {
    @Nullable
    private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }
    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }
    @Override
    public void registerIdleTransitionCallback(final ResourceCallback resourceCallback) {
        mCallback = resourceCallback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }

    public static void set(GalleryIdlingResource resource, boolean state) {
        if (resource != null) {
            resource.setIdleState(state);
        }
    }
}
