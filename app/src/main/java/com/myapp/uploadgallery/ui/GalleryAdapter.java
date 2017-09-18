package com.myapp.uploadgallery.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.model.GalleryImage;
import com.myapp.uploadgallery.util.DateFormatUtils;
import com.myapp.uploadgallery.util.UniqueList;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the gallery of uploaded images.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<GalleryImage> images = new UniqueList<>();

    public void setImages(List<GalleryImage> pics) {
        this.images.addAll(pics);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GalleryImage image = images.get(position);
        final Picasso picasso = Picasso.with(holder.cellImage.getContext());
        picasso.setLoggingEnabled(true);
        picasso.load(image.getUrl())
                .centerInside()
                .error(R.drawable.ic_image)
                .into(holder.cellImage);
        holder.cellTime.setText(DateFormatUtils.getFormattedTimestamp(image.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    /**
     * Stores and recycles views as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCellImage)
        ImageView cellImage;
        @BindView(R.id.tvCellTime)
        TextView cellTime;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
