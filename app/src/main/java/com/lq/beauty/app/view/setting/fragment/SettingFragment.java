package com.lq.beauty.app.view.setting.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lq.beauty.R;
import com.lq.beauty.app.BeautyApplication;
import com.lq.beauty.app.RxBus.RxBus;
import com.lq.beauty.app.RxBus.RxBusEvent;
import com.lq.beauty.app.SettingManager;
import com.lq.beauty.app.view.setting.adapter.SettingListAdapter;
import com.lq.beauty.app.view.setting.data.SettingItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.lq.beauty.base.fragment.BaseFragment;
import com.lq.beauty.base.ui.UIDialog;
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
                onItemClicked(settingListAdapter.getItem(position), position);
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
        list.add(new SettingItemData(
                SettingManager.SETTING_ID_DEFAULT_FILTER,
                mContext.getString(R.string.setFilter),
                SettingManager.getInstance().getDefaultFilterText(),
                SettingItemData.SWITCH_NONE,
                false));
        list.add(new SettingItemData(
                SettingManager.SETTING_ID_DEFAULT_CAMERA_RATIO,
                mContext.getString(R.string.setCameraRatio),
                SettingManager.getInstance().getCameraRatioText(),
                SettingItemData.SWITCH_NONE,
                false));
        list.add(new SettingItemData(
                SettingManager.SETTING_ID_DEFAULT_VIDEO_TIME,
                mContext.getString(R.string.setVideoTime),
                SettingManager.getInstance().getVideoTimeText(),
                SettingItemData.SWITCH_NONE,
                false));
        list.add(new SettingItemData(
                SettingManager.SETTING_ID_DEFAULT_WATERMARK,
                mContext.getString(R.string.setWatermark),
                SettingManager.getInstance().isWatermark() ? SettingManager.getInstance().getWatermarkText() : BeautyApplication.context().getString(R.string.watermarkClose),
                SettingItemData.SWITCH_NONE,
                false));
        list.add(new SettingItemData(
                SettingManager.SETTING_ID_DEFAULT_VIDEO_HD,
                mContext.getString(R.string.setVideoHD),
                "",
                SettingItemData.SWITCH_CLOSE,
                false));
        return list;
    }

    private void onItemClicked(final SettingItemData data, final int position) {
        if (null == data) return;
        switch (data.getID()) {
            case SettingManager.SETTING_ID_DEFAULT_FILTER:
                showSelecterDialog(
                        data.getTitle(),
                        resIdArrToTextArr(SettingManager.ARR_FILTER_TEXTS),
                        SettingManager.getInstance().getDefaultFilter(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingManager.getInstance().setDefaultFilter(which);
                                data.setSubTitle(SettingManager.getInstance().getDefaultFilterText());
                                settingListAdapter.updateItem(position);
                                dialog.dismiss();
                                RxBus.getInstance().post(new RxBusEvent(1));
                            }
                        }
                );
                break;
            case SettingManager.SETTING_ID_DEFAULT_CAMERA_RATIO:
                showSelecterDialog(
                        data.getTitle(),
                        resIdArrToTextArr(SettingManager.ARR_VIDEO_CAMERA_RATIO_TEXTS),
                        SettingManager.getInstance().getCameraRatio(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingManager.getInstance().setCameraRatio(which);
                                data.setSubTitle(SettingManager.getInstance().getCameraRatioText());
                                settingListAdapter.updateItem(position);
                                dialog.dismiss();
                                RxBus.getInstance().post(new RxBusEvent(2));
                            }
                        }
                );
                break;
            case SettingManager.SETTING_ID_DEFAULT_VIDEO_TIME:
                String[] items = new String[SettingManager.ARR_VIDEO_TIME.length];
                for (int i=0; i<items.length; i++) {
                    items[i] = SettingManager.getVideoTime(SettingManager.ARR_VIDEO_TIME[i]);
                }

                showSelecterDialog(
                        data.getTitle(),
                        items,
                        SettingManager.getInstance().getVideoTime(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingManager.getInstance().setVideoTime(which);
                                data.setSubTitle(SettingManager.getInstance().getVideoTimeText());
                                settingListAdapter.updateItem(position);
                                dialog.dismiss();

                            }
                        }
                );
                break;
            case SettingManager.SETTING_ID_DEFAULT_WATERMARK:
                RxBus.getInstance().post(new RxBusEvent(3));
                break;
            case SettingManager.SETTING_ID_DEFAULT_VIDEO_HD:
                data.setSwitchState(data.getSwitchState() == SettingItemData.SWITCH_CLOSE ? SettingItemData.SWITCH_OPEN : SettingItemData.SWITCH_CLOSE);
                SettingManager.getInstance().setHD(data.getSwitchState() == SettingItemData.SWITCH_OPEN);
                settingListAdapter.updateItem(position);
                break;
        }
    }

    private String[] resIdArrToTextArr(int[] resIds) {
        String[] ret = new String[resIds.length];
        for(int i=0; i<resIds.length; i++) {
            ret[i] = BeautyApplication.context().getString(resIds[i]);
        }

        return ret;
    }

    private Dialog showSelecterDialog(String title, String[] items, int selectIndex, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = UIDialog.createBaseDialog(mContext, title);
        builder.setSingleChoiceItems(items, selectIndex, clickListener);
        return builder.show();
    }

}
