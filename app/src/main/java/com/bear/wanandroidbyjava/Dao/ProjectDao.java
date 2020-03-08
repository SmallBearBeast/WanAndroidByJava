package com.bear.wanandroidbyjava.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.Bean.ProjectTab;
import com.bear.wanandroidbyjava.Entity.ArticleE;
import com.bear.wanandroidbyjava.Entity.ProjectTabArticleRef;
import com.bear.wanandroidbyjava.Entity.ProjectTabE;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class ProjectDao extends ArticleDao{
    @Query("SELECT * FROM ProjectTabE")
    abstract List<ProjectTabE> queryProjectTabE();

    @Query("SELECT ae.* FROM ArticleE AS ae INNER JOIN ProjectTabArticleRef AS ptar ON ae.articleId = ptar.articleId AND ptar.projectTabId = :projectTabId")
    abstract List<ArticleE> queryProjectArticleE(int projectTabId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertProjectTabE(List<ProjectTabE> projectTabE);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertProjectTabArticleRef(List<ProjectTabArticleRef> ProjectTabArticleRefList);

    @Query("DELETE FROM ProjectTabE")
    abstract void deleteProjectTabE();

    @Query("DELETE FROM ProjectTabArticleRef WHERE projectTabId = (:projectTabId)")
    abstract void deleteProjectTabArticleRef(int projectTabId);

    @Transaction
    public void deleteProjectTab() {
        deleteProjectTabE();
    }

    @Transaction
    public void deleteProjectArticle(int projectTabId) {
        List<ArticleE> deleteArticleEList = queryProjectArticleE(projectTabId);
        List<ArticleE> updateArticleEList = new ArrayList<>();
        List<Integer> deleteArticleEIdList = new ArrayList<>();
        for (ArticleE articleE : deleteArticleEList) {
            if (articleE.reference > 1) {
                articleE.reference--;
                updateArticleEList.add(articleE);
            } else {
                deleteArticleEIdList.add(articleE.articleId);
            }
        }
        deleteArticleE(deleteArticleEIdList);
        deleteProjectTabArticleRef(projectTabId);
        insertArticleE(updateArticleEList);
    }

    @Transaction
    public List<Article> queryProjectArticle(int projectTabId) {
        final List<ArticleE> projectArticleEList = queryProjectArticleE(projectTabId);
        final List<Article> projectArticleList = new ArrayList<>();
        for (ArticleE articleE : projectArticleEList) {
            projectArticleList.add(articleE.toArticle());
        }
        return projectArticleList;
    }

    @Transaction
    public List<ProjectTab> queryProjectTab() {
        List<ProjectTabE> projectTabEList = queryProjectTabE();
        List<ProjectTab> projectTabList = new ArrayList<>();
        for (ProjectTabE projectTabE : projectTabEList) {
            projectTabList.add(projectTabE.toProjectTab());
        }
        return projectTabList;
    }

    @Transaction
    public void insertProjectTab(List<ProjectTab> projectTabList) {
        List<ProjectTabE> projectTabEList = new ArrayList<>();
        for (ProjectTab projectTab : projectTabList) {
            projectTabEList.add(projectTab.toProjectTabE());
        }
        insertProjectTabE(projectTabEList);
    }

    @Transaction
    public void insertProjectArticle(int projectTabId, List<Article> articleList) {
        List<Integer> existArticleEIdList = new ArrayList<>();
        for (Article article : articleList) {
            existArticleEIdList.add(article.articleId);
        }
        List<ArticleE> existArticleEList = queryArticleE(existArticleEIdList);
        List<ProjectTabArticleRef> refList = new ArrayList<>();
        List<ArticleE> articleEList = new ArrayList<>();
        for (Article article : articleList) {
            ProjectTabArticleRef ref = new ProjectTabArticleRef();
            ref.projectTabId = projectTabId;
            ref.articleId = article.articleId;
            refList.add(ref);
            ArticleE articleE = article.toArticleE();
            for (ArticleE e : existArticleEList) {
                if (e.articleId == article.articleId) {
                    articleE.reference = e.reference + 1;
                    break;
                }
            }
            articleEList.add(articleE);
        }
        insertArticleE(articleEList);
        insertProjectTabArticleRef(refList);
    }
}
