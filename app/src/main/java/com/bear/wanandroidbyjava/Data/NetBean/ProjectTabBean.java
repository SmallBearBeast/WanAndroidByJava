package com.bear.wanandroidbyjava.Data.NetBean;

import com.bear.wanandroidbyjava.Data.Bean.ProjectTab;

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
        projectTab.projectTabId = id;
        projectTab.name = name;
        projectTab.order = order;
        return projectTab;
    }
}
