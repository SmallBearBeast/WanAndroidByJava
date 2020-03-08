package com.bear.wanandroidbyjava.Entity;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"articleId", "publicTabId"}, indices = @Index("publicTabId"))
public class PublicTabArticleRef {
    public int articleId;
    public int publicTabId;
}
