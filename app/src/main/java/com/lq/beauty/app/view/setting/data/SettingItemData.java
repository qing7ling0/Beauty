package com.lq.beauty.app.view.setting.data;

/**
 * Created by wuqingqing on 2017/3/30.
 */

public class SettingItemData {

    public final static short SWITCH_NONE = -1;
    public final static short SWITCH_CLOSE = 0;
    public final static short SWITCH_OPEN = 1;

    private String title;
    private String subTitle;
    private int ID;
    // -1:表示没有 0:表示关闭 1:表示打开
    private short switchState;
    // 是否有下一级
    private boolean hasNext;

    public SettingItemData(int id, String title, String subTitle, short switchState, boolean hasNext) {
        setID(id);
        setTitle(title);
        setSubTitle(subTitle);
        setSwitchState(switchState);
        setHasNext(hasNext);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public short getSwitchState() {
        return switchState;
    }

    public void setSwitchState(short switchState) {
        this.switchState = switchState;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
