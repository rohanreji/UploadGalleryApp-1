package com.myapp.uploadgallery.ui.manipulator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.manager.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

import static com.myapp.uploadgallery.util.FileUtils.getPictureFile;

/**
 * Fragment that contains view with manipulation abilities.
 */
public class ManipulatorFragment extends Fragment implements ManipulatorViewable {
    @BindView(R.id.cropImageView)
    CropImageView image;

    @BindView(R.id.progress)
    View progress;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    Picasso picasso;

    private ManipulatorListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_manipulator, container, false);
        ButterKnife.bind(this, view);

        image.setCropMode(CropImageView.CropMode.FREE);

        picasso.load(sharedPreferencesHelper.getImageUri())
                .placeholder(R.drawable.ic_image)
                .resize(800, 800)
                .centerInside()
                .into(image);

        return view;
    }

    @Override
    public void onAttach(final Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void setManipulatorListener(final ManipulatorListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.ivManipulatorCancel)
    public void close() {
        listener.onManipulatorClosed();
    }

    @OnClick(R.id.ivManipulatorSave)
    public void save() {
        progress.setVisibility(View.VISIBLE);
        final File pictureFile = getPictureFile(getContext());
        listener.onManipulatorCropped(pictureFile, image.cropAsSingle(Uri.fromFile(pictureFile)));
    }

    @OnClick(R.id.ivManipulatorRotateLeft)
    public void rotateLeft() {
        image.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D, 1000);
    }

    @OnClick(R.id.ivManipulatorRotateRight)
    public void rotateRight() {
        image.rotateImage(CropImageView.RotateDegrees.ROTATE_90D, 1000);
    }
}
