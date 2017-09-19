package com.myapp.uploadgallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ResizableImageView extends ImageView {
    public ResizableImageView(final Context context) {
        super(context);
    }

    public ResizableImageView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(final Context context, @Nullable final AttributeSet attrs,
                              final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
