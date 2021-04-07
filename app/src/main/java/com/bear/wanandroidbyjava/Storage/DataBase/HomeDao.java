package com.bear.wanandroidbyjava.Storage.DataBase;

import androidx.room.Dao;
import androidx.room.Query;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Entity.ArticleE;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class HomeDao extends ArticleDao {
    @Query("SELECT * FROM ArticleE WHERE category = 1 ORDER BY dateStr DESC")
    abstract List<ArticleE> queryMainArticleE();

    public List<Article> queryHomeArticle() {
        List<ArticleE> homeArticleEList = queryMainArticleE();
        List<Article> homeArticleList = new ArrayList<>();
        List<Article> topArticleList = new ArrayList<>();
        for (ArticleE articleE : homeArticleEList) {
            Article article = articleE.toArticle();
            if (article.top) {
                topArticleList.add(article);
            } else {
                homeArticleList.add(article);
            }
        }
        homeArticleList.addAll(0, topArticleList);
        return homeArticleList;
    }

    public void insertHomeArticle(List<Article> articleList) {
        List<Integer> existArticleEIdList = new ArrayList<>();
        for (Article article : articleList) {
            existArticleEIdList.add(article.articleId);
        }
        List<ArticleE> existArticleEList = queryArticleE(existArticleEIdList);
        List<ArticleE> articleEList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleE articleE = article.toArticleE();
            for (ArticleE e : existArticleEList) {
                if (e.articleId == article.articleId) {
                    articleE.reference = e.reference + 1;
                    break;
                }
            }
            articleE.category = ArticleE.CATEGORY_HOME;
            articleEList.add(articleE);
        }
        insertArticleE(articleEList);
    }
}
