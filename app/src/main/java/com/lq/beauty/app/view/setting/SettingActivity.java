package com.lq.beauty.app.view.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import com.lq.beauty.R;
import com.lq.beauty.app.base.BaseBackBeautyActivity;
import com.lq.beauty.app.view.setting.data.SettingItemData;
import com.lq.beauty.app.view.setting.fragment.SettingFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wuqingqing on 2017/3/29.
 */

public class SettingActivity extends BaseBackBeautyActivity {
    @BindView(R.id.flContent)
    FrameLayout flContent;

    SettingFragment settingFragment;

    @Override
    protected int getContentView() {
        return R.layout.act_setting;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        settingFragment = new SettingFragment();
        addFragment(R.id.flContent, settingFragment);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

}
