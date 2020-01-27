package com.bear.wanandroidbyjava.NetBean;

import com.bear.wanandroidbyjava.Bean.Article;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;

import java.util.List;

public class ArticleBean {
    public String apkLink;
    public int audit;
    public String author;
    public boolean canEdit;
    public int chapterId;
    public String chapterName;
    public boolean collect;
    public int courseId;
    public String desc;
    public String descMd;
    public String envelopePic;
    public boolean fresh;
    public int id;
    public String link;
    public String niceDate;
    public String niceShareDate;
    public String origin;
    public String prefix;
    public String projectLink;
    public long publishTime;
    public int selfVisible;
    public long shareDate;
    public String shareUser;
    public int superChapterId;
    public String superChapterName;
    public List<TagBean> tags;
    public String title;
    public int type;
    public int userId;
    public int visible;
    public int zan;

    public Article toArticle() {
        Article article = new Article();
        article.author = StringUtil.isEmpty(author) ? shareUser : author;
        article.chapterName = chapterName;
        article.superChapterName = superChapterName;
        article.collect = collect;
        article.fresh = fresh;
        article.dateStr = StringUtil.isEmpty(niceDate) ? niceShareDate : niceDate;
        article.title = title;
        article.description = desc;
        article.link = link;
        article.tag = CollectionUtil.isEmpty(tags) ? null : tags.get(0).name;
        return article;
    }
}
