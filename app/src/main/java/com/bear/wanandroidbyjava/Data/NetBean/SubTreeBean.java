package com.bear.wanandroidbyjava.Data.NetBean;

import com.bear.wanandroidbyjava.Data.Bean.SubTree;

public class SubTreeBean {
//    public String children;
    public int courseId;
    public int id;
    public String name;
    public int order;
    public int parentChapterId;
    public boolean userControlSetTop;
    public int visible;

    public SubTree toSubTree() {
        SubTree subTree = new SubTree();
        subTree.id = id;
        subTree.name = name;
        return subTree;
    }
}
