package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.myapp.uploadgallery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManipulatorFragment extends Fragment implements ManipulatorViewable {
    @BindView(R.id.cropImageView)
    CropImageView image;

    private ManipulatorListener listener;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_manipulator, container, false);
        ButterKnife.bind(this, view);

        if (null != listener) {
            listener.onViewCreated();
        }

        if (null != bitmap) {
            setBitmap(bitmap);
            bitmap = null;
        }

        return view;
    }

    @Override
    public void setBitmap(final Bitmap bitmap) {
        if (null != image) {
            image.setLoggingEnabled(true);
            image.setImageBitmap(bitmap);
        } else {
            this.bitmap = bitmap;
        }
    }

    @Override
    public void setManipulatorListener(final ManipulatorListener listener) {
        this.listener = listener;
    }
}
