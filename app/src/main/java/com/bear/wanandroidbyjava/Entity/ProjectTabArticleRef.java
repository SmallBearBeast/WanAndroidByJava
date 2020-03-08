package com.bear.wanandroidbyjava.Entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"articleId", "projectTabId"})
public class ProjectTabArticleRef {
    public int articleId;
    public int projectTabId;
}
