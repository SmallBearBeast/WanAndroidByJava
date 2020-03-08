package com.bear.wanandroidbyjava.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.bear.wanandroidbyjava.Bean.Article;

@Entity(indices = @Index("articleId"))
public class ArticleE {
    @PrimaryKey
    public int articleId;
    public String title;
    public String description;
    public String author;
    public String dateStr;
    public String chapterName;
    public String superChapterName;
    public String link;
    public String tag;
    public boolean fresh;
    public boolean collect;
    public boolean top;
    public int reference;
    public long insertTime;

    public Article toArticle() {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.description = description;
        article.author = author;
        article.dateStr = dateStr;
        article.chapterName = chapterName;
        article.superChapterName = superChapterName;
        article.link = link;
        article.tag = tag;
        article.fresh = fresh;
        article.collect = collect;
        article.top = top;
        return article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleE articleE = (ArticleE) o;
        return articleId == articleE.articleId;
    }
}
