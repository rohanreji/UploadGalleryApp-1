package com.myapp.uploadgallery.ui.manipulator;

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
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.myapp.uploadgallery.util.FileUtils.getPictureFile;

/**
 * Fragment that contains view with manipulation abilities.
 */
public class ManipulatorFragment extends Fragment implements ManipulatorViewable {
    @BindView(R.id.cropImageView)
    CropImageView image;

    @BindView(R.id.progress)
    View progress;

    private ManipulatorListener listener;
    private ManipulatorUiListener uiListener;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_manipulator, container, false);
        ButterKnife.bind(this, view);

        image.setCropMode(CropImageView.CropMode.FREE);

        if (null != bitmap) {
            setBitmapToManipulate(bitmap);
            bitmap = null;
        }

        return view;
    }

    @Override
    public void setBitmapToManipulate(final Bitmap bitmap) {
        if (null != image) {
            image.setLoggingEnabled(true);
            image.setImageBitmap(bitmap);
        } else {
            this.bitmap = bitmap;
        }
    }

    @Override
    public void setManipulatorListeners(final ManipulatorListener listener,
                                        final ManipulatorUiListener uilistener) {
        this.listener = listener;
        this.uiListener = uilistener;
    }

    @OnClick(R.id.ivManipulatorCancel)
    public void close() {
        if (null != uiListener) {
            uiListener.closeManipulator();
        }
    }

    @OnClick(R.id.ivManipulatorSave)
    public void save() {
        progress.setVisibility(View.VISIBLE);
        image.cropAsSingle()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws
                            Exception {
                        if (null != uiListener) {
                            uiListener.closeManipulator();
                        }
                        if (null != listener) {
                            listener.onManipulatorCropped(getPictureFile(getContext()), bitmap);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
                            throws Exception {
                        if (null != uiListener) {
                            uiListener.onManipulationError();
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
