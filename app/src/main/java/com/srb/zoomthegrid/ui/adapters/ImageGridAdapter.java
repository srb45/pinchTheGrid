package com.srb.zoomthegrid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.srb.zoomthegrid.R;
import com.srb.zoomthegrid.db.models.Picture;
import com.srb.zoomthegrid.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ViewHolder> {

    private List<Picture> mPictures = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UiUtils.displayImage(holder.image, mPictures.get(position).getThumbnailUrl());
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public void setPictures(List<Picture> pictures) {
        mPictures = pictures;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = UiUtils.getView(itemView, R.id.ITEM_small_img);
        }
    }
}
