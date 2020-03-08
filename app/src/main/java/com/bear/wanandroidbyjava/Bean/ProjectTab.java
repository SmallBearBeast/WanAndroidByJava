package com.bear.wanandroidbyjava.Bean;

import com.bear.wanandroidbyjava.Entity.ProjectTabE;

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

