package com.bear.wanandroidbyjava.Data.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.bear.wanandroidbyjava.Data.Entity.ArticleE;
import com.bear.wanandroidbyjava.Module.Collect.CollectInfo;
import com.example.libbase.ExtObj;

public class Article extends ExtObj implements Parcelable {
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

    public Article() {

    }

    protected Article(Parcel in) {
        articleId = in.readInt();
        title = in.readString();
        description = in.readString();
        author = in.readString();
        dateStr = in.readString();
        chapterName = in.readString();
        superChapterName = in.readString();
        link = in.readString();
        tag = in.readString();
        fresh = in.readByte() != 0;
        collect = in.readByte() != 0;
        top = in.readByte() != 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

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
                ", author='" + author + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(articleId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeString(dateStr);
        dest.writeString(chapterName);
        dest.writeString(superChapterName);
        dest.writeString(link);
        dest.writeString(tag);
        dest.writeByte((byte) (fresh ? 1 : 0));
        dest.writeByte((byte) (collect ? 1 : 0));
        dest.writeByte((byte) (top ? 1 : 0));
    }

    public void update(Article article) {
        if (articleId == article.articleId) {
            title = article.title;
            description = article.description;
            author = article.author;
            dateStr = article.dateStr;
            chapterName = article.chapterName;
            superChapterName = article.superChapterName;
            link = article.link;
            tag = article.tag;
            fresh = article.fresh;
            collect = article.collect;
            top = article.top;
        }
    }

    public CollectInfo toCollectInfo() {
        CollectInfo.Builder builder = new CollectInfo.Builder();
        builder.collectId(articleId);
        builder.deleteId(articleId);
        builder.collect(collect);
        builder.author(author);
        builder.link(link);
        builder.title(title);
        builder.fromType(CollectInfo.TYPE_ARTICLE);
        return builder.build();

    }
}
