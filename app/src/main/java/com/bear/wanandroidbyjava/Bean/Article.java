package com.bear.wanandroidbyjava.Bean;

import com.example.libbase.ExtObj;

public class Article extends ExtObj {
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

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
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
