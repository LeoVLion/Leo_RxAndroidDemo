package com.example.administrator.leo_rxandroiddemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.leo_rxandroiddemo.R;
import com.example.administrator.leo_rxandroiddemo.model.ZhuangbiImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class ZhuangbiListAdapter extends RecyclerView.Adapter<ZhuangbiListAdapter.MyViewHolder> {

    private List<ZhuangbiImage> images;

    public ZhuangbiListAdapter() {
    }

    public ZhuangbiListAdapter(List<ZhuangbiImage> images) {
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ZhuangbiImage image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(holder.rv_item_iv);
        holder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setImages(List<ZhuangbiImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rv_item_iv)
        ImageView rv_item_iv;
        @Bind(R.id.descriptionTv)
        TextView descriptionTv;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
