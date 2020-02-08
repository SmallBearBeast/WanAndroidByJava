package com.bear.wanandroidbyjava.NetBean;

import com.bear.wanandroidbyjava.Bean.ProjectTab;

public class ProjectTabBean {
    //            public List children;
    public int courseId;
    public int id;
    public String name;
    public int order;
    public int parentChapterId;
    public boolean userControlSetTop;
    public int visible;

    public ProjectTab toProjectTab() {
        ProjectTab projectTab = new ProjectTab();
        projectTab.id = id;
        projectTab.name = name;
        return projectTab;
    }
}
