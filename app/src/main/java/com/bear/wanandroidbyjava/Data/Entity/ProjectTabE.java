package com.bear.wanandroidbyjava.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bear.wanandroidbyjava.Data.Bean.ProjectTab;

@Entity
public class ProjectTabE {
    @PrimaryKey
    public int projectTabId;
    public String name;
    public int order;

    public ProjectTab toProjectTab() {
        ProjectTab projectTab = new ProjectTab();
        projectTab.projectTabId = projectTabId;
        projectTab.name = name;
        projectTab.order = order;
        return projectTab;
    }
}
