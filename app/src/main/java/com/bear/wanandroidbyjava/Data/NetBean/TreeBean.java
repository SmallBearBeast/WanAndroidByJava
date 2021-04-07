package com.bear.wanandroidbyjava.Data.NetBean;

import com.bear.wanandroidbyjava.Data.Bean.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeBean {
    public int courseId;
    public int id;
    public String name;
    public int order;
    public int parentChapterId;
    public boolean userControlSetTop;
    public int visible;
    public List<SubTreeBean> children;

    public Tree toTree() {
        Tree tree = new Tree();
        tree.name = name;
        tree.subTreeList = new ArrayList<>();
        for (SubTreeBean child : children) {
            tree.subTreeList.add(child.toSubTree());
        }
        return tree;
    }
}
