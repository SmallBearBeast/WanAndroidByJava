package com.bear.wanandroidbyjava.Storage.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bear.wanandroidbyjava.Data.Entity.ArticleE;

import java.util.List;

@Dao
public abstract class ArticleDao {
    @Query("SELECT * FROM ArticleE WHERE articleId IN (:articleIdList)")
    abstract List<ArticleE> queryArticleE(List<Integer> articleIdList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertArticleE(List<ArticleE> articleEList);

    @Query("DELETE FROM ArticleE WHERE articleId IN (:articleIdList) AND reference < 1")
    abstract void deleteArticleE(List<Integer> articleIdList);

    @Update
    abstract void updateArticleE(List<ArticleE> articleEList);
}
