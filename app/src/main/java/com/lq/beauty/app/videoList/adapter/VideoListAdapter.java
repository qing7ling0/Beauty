package com.lq.beauty.app.videoList.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lq.beauty.R;
import com.lq.beauty.app.videoList.data.VideoListItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/25.
 */

public class VideoListAdapter extends BaseRecyclerAdapter<VideoListItemData> {
    public VideoListAdapter(Context context) {
        super(context, NEITHER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_video_list, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, VideoListItemData item, int position) {
        ViewHolder _holder = (ViewHolder) holder;
        _holder.tvVideoListTime.setText(item.getTime());
        if (item.getBitmap() != null)
            _holder.ivVideoListIcon.setImageBitmap(item.getBitmap());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivVideoListIcon)
        ImageView ivVideoListIcon;
        @BindView(R.id.tvVideoListTime)
        TextView tvVideoListTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
