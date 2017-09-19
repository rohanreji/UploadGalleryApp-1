package com.myapp.uploadgallery.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.myapp.uploadgallery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ManipulatorFragment extends Fragment implements ManipulatorViewable {
    @BindView(R.id.cropImageView)
    CropImageView image;

    private ManipulatorListener listener;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_manipulator, container, false);
        ButterKnife.bind(this, view);

        image.setCropMode(CropImageView.CropMode.FREE);
        image.setDebug(true);

        if (null != bitmap && null != uri) {
            setBitmap(bitmap, uri);
            bitmap = null;
        }

        return view;
    }

    @Override
    public void setBitmap(final Bitmap bitmap, final Uri toSave) {
        if (null != image) {
            image.setLoggingEnabled(true);
            image.setImageBitmap(bitmap);
        } else {
            this.bitmap = bitmap;
        }
        this.uri = toSave;
    }

    @Override
    public void setManipulatorListener(final ManipulatorListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.ivManipulatorCancel)
    public void close() {
        if (null != listener) {
            listener.close();
        }
    }

    @OnClick(R.id.ivManipulatorSave)
    public void save() {
        if (null != listener) {
            listener.showProgress();
        }
        image.cropAsSingle()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        if (null != listener) {
                            listener.onCropped(bitmap);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
                            throws Exception {
                        if (null != listener) {
                            listener.onError();
                        }
                    }
                });
    }

    @OnClick(R.id.ivManipulatorRotateLeft)
    public void rotateLeft() {
        if (null != image) {
            image.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D, 1000);
        }
    }

    @OnClick(R.id.ivManipulatorRotateRight)
    public void rotateRight() {
        if (null != image) {
            image.rotateImage(CropImageView.RotateDegrees.ROTATE_90D, 1000);
        }
    }
}
