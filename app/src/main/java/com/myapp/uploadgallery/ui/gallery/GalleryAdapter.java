package com.myapp.uploadgallery.ui.gallery;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.util.DateFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the gallery of uploaded images.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private SortedList<GalleryImage> images;
    private Picasso picasso;

    public GalleryAdapter(Picasso picasso) {
        super();

        this.picasso = picasso;
        picasso.setLoggingEnabled(true);

        images = new SortedList<>(GalleryImage.class,
                new SortedList.Callback<GalleryImage>() {
                    @Override
                    public int compare(final GalleryImage o1, final GalleryImage o2) {
                        return o1.compareTo(o2);
                    }

                    @Override
                    public void onChanged(final int position, final int count) {
                        notifyItemRangeChanged(position, count);
                    }
                    @Override
                    public boolean areContentsTheSame(final GalleryImage oldItem,
                                                      final GalleryImage newItem) {
                        return oldItem.getUrl().equals(newItem.getUrl());
                    }
                    @Override
                    public boolean areItemsTheSame(final GalleryImage item1,
                                                   final GalleryImage item2) {
                        return item1.getCreatedAt().equals(item2.getCreatedAt());
                    }
                    @Override
                    public void onInserted(final int position, final int count) {
                        notifyItemRangeInserted(position, count);
                    }
                    @Override
                    public void onRemoved(final int position, final int count) {
                        notifyItemRangeRemoved(position, count);
                    }
                    @Override
                    public void onMoved(final int fromPosition, final int toPosition) {
                        notifyItemMoved(fromPosition, toPosition);
                    }
                });
    }

    /**
     * Adds new images to the adapter.
     *
     * @param pics images to add
     */
    public void setImages(List<GalleryImage> pics) {
        this.images.addAll(pics);
    }

    public void addImage(GalleryImage image) {
        this.images.add(image);
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
        picasso.load(image.getUrl())
                .placeholder(R.drawable.ic_image)
                .resize(800, 800)
                .centerInside()
                .into(holder.cellImage);
        final long createdAtTimestamp = DateFormatUtils.parseTime(image.getCreatedAt());
        final CharSequence formattedTimestamp =
                DateFormatUtils.getFormattedTimestamp(createdAtTimestamp);
        holder.cellTime.setText(formattedTimestamp);
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
