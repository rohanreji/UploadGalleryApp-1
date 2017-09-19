package com.myapp.uploadgallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.api.GalleryImage;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Has a gallery view with already uploaded images.
 */
public class GalleryFragment extends Fragment implements GalleryViewable {
    @BindView(R.id.rvGallery)
    RecyclerView galleryView;

    private GalleryAdapter adapter;
    private GalleryListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_gallery, container, false);
        ButterKnife.bind(this, view);

        galleryView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new GalleryAdapter();
        galleryView.setAdapter(adapter);

        if (listener != null) {
            listener.onViewCreated();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setImages(final Set<GalleryImage> images) {
        if (null != adapter) {
            adapter.setImages(images);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCallback(final GalleryListener galleryListener) {
        this.listener = galleryListener;
    }
}
