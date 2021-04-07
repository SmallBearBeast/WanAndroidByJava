package com.bear.wanandroidbyjava.Data.Entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"articleId", "projectTabId"})
public class ProjectTabArticleRef {
    public int articleId;
    public int projectTabId;
}
