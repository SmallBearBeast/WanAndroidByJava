package com.bear.wanandroidbyjava.Data.Bean;

import com.bear.wanandroidbyjava.Data.Entity.ProjectTabE;

public class ProjectTab {
    public int projectTabId;
    public String name;
    public int order;

    public ProjectTabE toProjectTabE() {
        ProjectTabE projectTabE = new ProjectTabE();
        projectTabE.projectTabId = projectTabId;
        projectTabE.name = name;
        projectTabE.order = order;
        return projectTabE;
    }
}

