package com.bear.wanandroidbyjava.Bean;

import com.bear.wanandroidbyjava.Entity.ArticleE;
import com.example.libbase.ExtObj;

public class Article extends ExtObj {
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

    public ArticleE toArticleE() {
        ArticleE articleE = new ArticleE();
        articleE.articleId = articleId;
        articleE.title = title;
        articleE.description = description;
        articleE.author = author;
        articleE.dateStr = dateStr;
        articleE.chapterName = chapterName;
        articleE.superChapterName = superChapterName;
        articleE.link = link;
        articleE.tag = tag;
        articleE.fresh = fresh;
        articleE.collect = collect;
        articleE.top = top;
        articleE.reference = 1;
        return articleE;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", dateStr='" + dateStr + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", superChapterName='" + superChapterName + '\'' +
                ", link='" + link + '\'' +
                ", tag='" + tag + '\'' +
                ", fresh=" + fresh +
                ", collect=" + collect +
                ", top=" + top +
                '}';
    }
}
