package com.lq.beauty.app.view.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.lq.beauty.R;
import com.lq.beauty.app.view.main.data.MenuItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.mikepenz.iconics.view.IconicsTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuqingqing on 2017/3/25.
 */

public class MenuListAdapter extends BaseRecyclerAdapter<MenuItemData> {
    public MenuListAdapter(Context context) {
        super(context, NEITHER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_menu, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, MenuItemData item, int position) {
        ViewHolder _holder = (ViewHolder) holder;
        _holder.itvMenuIcon.setText(item.getIcon());
        _holder.tvMenuTitle.setText(item.getTitleResID());
        if (item.getSwitchState() == MenuItemData.SWITCH_NONE) {
            _holder.menuSwitch.setVisibility(View.GONE);
        } else {
            _holder.menuSwitch.setChecked(item.getSwitchState() == MenuItemData.SWITCH_OPEN);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itvMenuIcon)
        IconicsTextView itvMenuIcon;
        @BindView(R.id.tvMenuTitle)
        TextView tvMenuTitle;
        @BindView(R.id.menuSwitch)
        Switch menuSwitch;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
