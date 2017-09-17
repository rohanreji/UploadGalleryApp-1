package com.myapp.uploadgallery.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.model.UpImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the gallery of uploaded images.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<UpImage> images;
    private LayoutInflater inflater;
    private Context context;

    public GalleryAdapter(Context context, List<UpImage> images) {
        this.inflater = LayoutInflater.from(context);
        this.images = images;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.gallery_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UpImage image = images.get(position);
        Picasso.with(context)
                .load(image.getLocation())
                .resize(120, 60)
                .into(holder.cellImage);
        holder.cellTime.setText(image.getFormattedTimestamp());

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
        @BindView(R.id.tvCellTime) TextView cellTime;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}