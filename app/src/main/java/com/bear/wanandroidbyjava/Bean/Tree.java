package com.bear.wanandroidbyjava.Bean;

import java.util.List;

public class Tree {
    public String name;
    public List<SubTree> subTreeList;

    @Override
    public String toString() {
        return "Tree{" +
                "name='" + name + '\'' +
                ", subTreeList=" + subTreeList +
                '}';
    }
}
