package com.lq.beauty.app.videoList.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lq.beauty.R;
import com.lq.beauty.app.videoList.VideoListConfig;
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
        _holder.setVideoPath(item.getVideoPath());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivVideoListIcon)
        SimpleDraweeView ivVideoListIcon;
        @BindView(R.id.tvVideoListTime)
        TextView tvVideoListTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setVideoPath(String path) {
            path = "file://" + path;
//            path = "http://img0.imgtn.bdimg.com/it/u=790765524,1350374988&fm=23&gp=0.jpg";
            Uri uri = (path != null) ? Uri.parse(path) : null;
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(VideoListConfig.VIDEO_THUMBNAIL_WIDTH, VideoListConfig.VIDEO_THUMBNAIL_HEIGHT))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder() .setOldController(ivVideoListIcon.getController()) .setImageRequest(request).build();
            ivVideoListIcon.setController(controller);
        }
    }
}
