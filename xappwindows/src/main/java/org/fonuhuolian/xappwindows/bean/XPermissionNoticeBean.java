package org.fonuhuolian.xappwindows.bean;

public class XPermissionNoticeBean {
    private int icon;
    private String pName;
    private String pDescribe;
    private String manifestPermission;

    public XPermissionNoticeBean(int icon, String pName, String pDescribe, String manifestPermission) {
        this.icon = icon;
        this.pName = pName;
        this.pDescribe = pDescribe;
        this.manifestPermission = manifestPermission;
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

    public String getManifestPermission() {
        return manifestPermission;
    }
}
