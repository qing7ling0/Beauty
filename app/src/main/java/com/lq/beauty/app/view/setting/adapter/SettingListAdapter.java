package com.lq.beauty.app.view.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.lq.beauty.R;
import com.lq.beauty.app.view.main.data.MenuItemData;
import com.lq.beauty.app.view.setting.data.SettingItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.mikepenz.iconics.view.IconicsTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuqingqing on 2017/3/25.
 */

public class SettingListAdapter extends BaseRecyclerAdapter<SettingItemData> {
    public SettingListAdapter(Context context) {
        super(context, NEITHER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_setting, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, SettingItemData item, int position) {
        ViewHolder _holder = (ViewHolder) holder;
        _holder.tvTitle.setText(item.getTitle());
        _holder.tvSubTitle.setVisibility(TextUtils.isEmpty(item.getSubTitle()) ? View.GONE : View.VISIBLE);
        _holder.tvSubTitle.setText(item.getSubTitle());
        if (item.getSwitchState() == MenuItemData.SWITCH_NONE) {
            _holder.sSwitch.setVisibility(View.GONE);
        } else {
            _holder.sSwitch.setChecked(item.getSwitchState() == MenuItemData.SWITCH_OPEN);
        }
        _holder.itvNextIcon.setVisibility(item.isHasNext() ? View.VISIBLE : View.GONE);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itvNextIcon)
        IconicsTextView itvNextIcon;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSubTitle)
        TextView tvSubTitle;
        @BindView(R.id.sSwitch)
        Switch sSwitch;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
