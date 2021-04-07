package com.bear.wanandroidbyjava.Storage.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.PublicTab;
import com.bear.wanandroidbyjava.Data.Entity.ArticleE;
import com.bear.wanandroidbyjava.Data.Entity.PublicTabArticleRef;
import com.bear.wanandroidbyjava.Data.Entity.PublicTabE;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class PublicDao extends ArticleDao{
    @Query("SELECT * FROM PublicTabE")
    abstract List<PublicTabE> queryPublicTabE();

    @Query("SELECT ae.* FROM ArticleE AS ae INNER JOIN PublicTabArticleRef AS ptar ON ae.articleId = ptar.articleId AND ptar.publicTabId = :publicTabId")
    abstract List<ArticleE> queryPublicArticleE(int publicTabId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertPublicTabE(List<PublicTabE> publicTabE);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertPublicTabArticleRef(List<PublicTabArticleRef> publicTabArticleRefList);

    @Query("DELETE FROM PublicTabE")
    abstract void deletePublicTabE();

    @Query("DELETE FROM PublicTabArticleRef WHERE publicTabId = (:publicTabId)")
    abstract void deletePublicTabArticleRef(int publicTabId);

    @Transaction
    public void deletePublicTab() {
        deletePublicTabE();
    }

    @Transaction
    public void deletePublicArticle(int publicTabId) {
        List<ArticleE> deleteArticleEList = queryPublicArticleE(publicTabId);
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
        deletePublicTabArticleRef(publicTabId);
        updateArticleE(updateArticleEList);
    }

    @Transaction
    public List<Article> queryPublicArticle(int publicTabId) {
        List<ArticleE> publicArticleEList = queryPublicArticleE(publicTabId);
        List<Article> publicArticleList = new ArrayList<>();
        for (ArticleE articleE : publicArticleEList) {
            publicArticleList.add(articleE.toArticle());
        }
        return publicArticleList;
    }

    @Transaction
    public List<PublicTab> queryPublicTab() {
        List<PublicTabE> publicTabEList = queryPublicTabE();
        List<PublicTab> publicTabList = new ArrayList<>();
        for (PublicTabE publicTabE : publicTabEList) {
            publicTabList.add(publicTabE.toPublicTab());
        }
        return publicTabList;
    }

    @Transaction
    public void insertPublicTab(List<PublicTab> publicTabList) {
        List<PublicTabE> publicTabEList = new ArrayList<>();
        for (PublicTab publicTab : publicTabList) {
            publicTabEList.add(publicTab.toPublicTabE());
        }
        insertPublicTabE(publicTabEList);
    }

    @Transaction
    public void insertPublicArticle(int publicTabId, List<Article> articleList) {
        List<Integer> existArticleEIdList = new ArrayList<>();
        for (Article article : articleList) {
            existArticleEIdList.add(article.articleId);
        }
        List<ArticleE> existArticleEList = queryArticleE(existArticleEIdList);
        List<PublicTabArticleRef> refList = new ArrayList<>();
        List<ArticleE> articleEList = new ArrayList<>();
        for (Article article : articleList) {
            PublicTabArticleRef ref = new PublicTabArticleRef();
            ref.publicTabId = publicTabId;
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
        insertPublicTabArticleRef(refList);
    }
}
