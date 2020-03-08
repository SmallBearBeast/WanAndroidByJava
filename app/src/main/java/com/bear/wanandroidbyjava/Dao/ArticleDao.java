package com.bear.wanandroidbyjava.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.bear.wanandroidbyjava.Entity.ArticleE;

import java.util.List;

@Dao
public abstract class ArticleDao {
    @Query("SELECT * FROM ArticleE WHERE articleId IN (:articleIdList)")
    abstract List<ArticleE> queryArticleE(List<Integer> articleIdList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertArticleE(List<ArticleE> articleEList);

    @Query("DELETE FROM ArticleE WHERE articleId IN (:articleIdList) AND reference < 1")
    abstract void deleteArticleE(List<Integer> articleIdList);
}
