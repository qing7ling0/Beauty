package com.lq.beauty.app.view.main;

import android.support.v7.widget.LinearLayoutManager;

import com.lq.beauty.R;
import com.lq.beauty.app.view.main.adapter.MenuListAdapter;
import com.lq.beauty.app.view.main.data.MenuItemData;
import com.lq.beauty.app.view.setting.SettingActivity;
import com.lq.beauty.app.view.videoList.data.VideoListItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.lq.beauty.base.widget.BaseRecyclerView;

/**
 * Created by wuqingqing on 2017/3/29.
 */

public class MainMenu {
    public final static int MENU_ID_ABOUT = 1;
    public final static int MENU_ID_UPDATE = 2;
    public final static int MENU_ID_WELCOME = 3;
    public final static int MENU_ID_SETTING = 4;
    public final static int MENU_ID_SHARE = 5;

    private final static MenuItemData[] MENU_ITEMS = {
            new MenuItemData(MENU_ID_SETTING, "{cmd-settings}", R.string.menuSetting, MenuItemData.SWITCH_NONE),
            new MenuItemData(MENU_ID_ABOUT, "{cmd-contacts}", R.string.menuAbout, MenuItemData.SWITCH_NONE),
            new MenuItemData(MENU_ID_WELCOME, "{cmd-eye-outline}", R.string.menuWelcome, MenuItemData.SWITCH_NONE),
            new MenuItemData(MENU_ID_UPDATE, "{cmd-update}", R.string.menuUpdate, MenuItemData.SWITCH_NONE),
            new MenuItemData(MENU_ID_SHARE, "{cmd-share-variant}", R.string.menuShare, MenuItemData.SWITCH_NONE),
    };

    BaseRecyclerView brvMenuList;

    private MainActivity activity;

    private MenuListAdapter menuListAdapter;

    public MainMenu(MainActivity activity, BaseRecyclerView brvMenuList) {
        this.activity = activity;
        this.brvMenuList = brvMenuList;
    }

    public void initWidget() {
        menuListAdapter = new MenuListAdapter(activity);
        menuListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                MenuItemData data = menuListAdapter.getItem(position);
                if (null != data) {
                    onItemClicked(data);
                }
            }
        });

        brvMenuList.setLayoutManager(new LinearLayoutManager(activity));
        brvMenuList.setAdapter(menuListAdapter);
    }

    public void initData() {
        for(int i=0; i<MENU_ITEMS.length; i++) {
            menuListAdapter.addItem(MENU_ITEMS[i]);
        }
    }

    private void onItemClicked(MenuItemData data) {
        if (data == null) return;

        switch (data.getID()) {
            case MENU_ID_ABOUT:
                break;
            case MENU_ID_UPDATE:
                break;
            case MENU_ID_WELCOME:
                break;
            case MENU_ID_SETTING:
                SettingActivity.show(activity);
                break;
            case MENU_ID_SHARE:
                break;
        }
    }
}
