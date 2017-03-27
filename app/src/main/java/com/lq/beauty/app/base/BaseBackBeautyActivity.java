package com.lq.beauty.app.base;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lq.beauty.R;

/**
 * Created by wuqingqing on 2017/3/27.
 */

public abstract class BaseBackBeautyActivity extends BaseBeautyActivity {

    protected Button btnBack;
    protected TextView tvBackTitle;

    @Override
    protected void initWindow() {
        super.initWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        btnBack = (Button) findViewById(R.id.btnBack);
        tvBackTitle = (TextView) findViewById(R.id.tvBackTitle);
        if (null != btnBack) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClicked();
                }
            });
        }
    }

    public void setTitle(CharSequence title) {
        if (null != tvBackTitle) tvBackTitle.setText(title);
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    private void onBackClicked() {
        finish();
    }
}
