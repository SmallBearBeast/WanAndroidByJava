package com.bear.wanandroidbyjava.NetBean;

import com.bear.wanandroidbyjava.Bean.PublicTab;

public class PublicTabBean {
    //            public List children;
    public int courseId;
    public int id;
    public String name;
    public int order;
    public int parentChapterId;
    public boolean userControlSetTop;
    public int visible;

    public PublicTab toPublicTab() {
        PublicTab publicTab = new PublicTab();
        publicTab.publicTabId = id;
        publicTab.name = name;
        publicTab.order = order;
        return publicTab;
    }
}
