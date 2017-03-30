package com.lq.beauty.app.view.setting.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lq.beauty.R;
import com.lq.beauty.app.view.setting.adapter.SettingListAdapter;
import com.lq.beauty.app.view.setting.data.SettingItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.lq.beauty.base.fragment.BaseFragment;
import com.lq.beauty.base.widget.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wuqingqing on 2017/3/30.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R.id.brvSettingList)
    BaseRecyclerView brvSettingList;

    private SettingListAdapter settingListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_setting;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        settingListAdapter = new SettingListAdapter(mContext);
        settingListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {

            }
        });
        brvSettingList.setLayoutManager(new LinearLayoutManager(mContext));
        brvSettingList.setAdapter(settingListAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        settingListAdapter.addAll(getListData());
    }

    private List<SettingItemData> getListData() {
        List<SettingItemData> list = new ArrayList<SettingItemData>();
//        list.add(new SettingItemData());
        return list;
    }

}
