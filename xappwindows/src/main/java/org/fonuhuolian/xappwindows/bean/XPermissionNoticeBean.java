package org.fonuhuolian.xappwindows.bean;

public class XPermissionNoticeBean {
    private int icon;
    private String pName;
    private String pDescribe;

    public XPermissionNoticeBean(int icon, String pName, String pDescribe) {
        this.icon = icon;
        this.pName = pName;
        this.pDescribe = pDescribe;
    }

    public int getIcon() {
        return icon;
    }

    public String getpName() {
        return pName;
    }

    public String getpDescribe() {
        return pDescribe;
    }
}
