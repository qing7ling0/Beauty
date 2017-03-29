package com.lq.beauty.app.main.data;

/**
 * Created by wuqingqing on 2017/3/29.
 */

public class MenuItemData {
    public final static short SWITCH_NONE = -1;
    public final static short SWITCH_CLOSE = 0;
    public final static short SWITCH_OPEN = 1;

    private String icon;
    private int titleResID;
    // -1:表示没有 0:表示关闭 1:表示打开
    private short switchState;

    public MenuItemData(String icon, int titleResID, short switchState) {
        setIcon(icon);
        setTitleResID(titleResID);
        setSwitchState(switchState);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public short getSwitchState() {
        return switchState;
    }

    public void setSwitchState(short switchState) {
        this.switchState = switchState;
    }

    public int getTitleResID() {
        return titleResID;
    }

    public void setTitleResID(int titleResID) {
        this.titleResID = titleResID;
    }
}
