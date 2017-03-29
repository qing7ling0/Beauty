package com.lq.beauty.app.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lq.beauty.R;
import com.lq.beauty.app.main.adapter.MenuListAdapter;
import com.lq.beauty.app.main.data.MenuItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.lq.beauty.base.widget.BaseRecyclerView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            new MenuItemData("{cmd-settings}", R.string.menuSetting, MenuItemData.SWITCH_OPEN),
            new MenuItemData("{cmd-contacts}", R.string.menuAbout, MenuItemData.SWITCH_NONE),
            new MenuItemData("{cmd-eye-outline}", R.string.menuWelcome, MenuItemData.SWITCH_NONE),
            new MenuItemData("{cmd-update}", R.string.menuUpdate, MenuItemData.SWITCH_NONE),
            new MenuItemData("{cmd-share-variant}", R.string.menuShare, MenuItemData.SWITCH_NONE),
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

            }
        });

        brvMenuList.setLayoutManager(new LinearLayoutManager(activity));
        brvMenuList.setAdapter(menuListAdapter);
        brvMenuList.setFosucInterceptTouchEvent(true);
    }

    public void initData() {
        for(int i=0; i<MENU_ITEMS.length; i++) {
            menuListAdapter.addItem(MENU_ITEMS[i]);
        }
    }
}
