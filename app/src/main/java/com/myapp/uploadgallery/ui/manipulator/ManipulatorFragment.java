package com.myapp.uploadgallery.ui.manipulator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.manager.SharedPreferencesHelper;
import com.myapp.uploadgallery.test.GalleryIdlingResource;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
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

    @BindView(R.id.pbManipulator)
    View progress;

    @BindViews({R.id.ivManipulatorCancel, R.id.ivManipulatorRotateLeft,
            R.id.ivManipulatorRotateRight, R.id.ivManipulatorSave})
    List<View> controlViews;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    Picasso picasso;

    private ManipulatorListener listener;
    private GalleryIdlingResource idlingResource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_manipulator, container, false);
        ButterKnife.bind(this, view);

        image.setCropMode(CropImageView.CropMode.FREE);

        GalleryIdlingResource.set(idlingResource, true);
        picasso.load(sharedPreferencesHelper.getImageUri())
                .placeholder(R.drawable.ic_image)
                .resize(800, 800)
                .centerInside()
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        GalleryIdlingResource.set(idlingResource, false);
                    }
                    @Override
                    public void onError() {
                        GalleryIdlingResource.set(idlingResource, false);
                    }
                });

        return view;
    }

    @Override
    public void onAttach(final Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void setManipulatorListener(@Nullable final GalleryIdlingResource idlingResource,
                                       final ManipulatorListener listener) {
        this.listener = listener;
        this.idlingResource = idlingResource;
    }

    @OnClick(R.id.ivManipulatorCancel)
    public void close() {
        listener.onManipulatorClosed();
    }

    @OnClick(R.id.ivManipulatorSave)
    public void save() {
        GalleryIdlingResource.set(idlingResource, true);
        progress.setVisibility(View.VISIBLE);
        ButterKnife.apply(controlViews, new ButterKnife.Action<View>() {
            @Override
            public void apply(@NonNull final View view, final int index) {
                view.setEnabled(false);
                view.setClickable(false);
            }
        });
        listener.onManipulatorCropped(idlingResource,
                getPictureFile(getContext()), image.cropAsSingle());
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
