package com.bear.wanandroidbyjava.Module.Collect;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectInfo implements Parcelable {
    public static final int INVALID_ID = -1;
    public static final int TYPE_ARTICLE = 1;
    public static final int TYPE_BANNER = 2;
    public static final int TYPE_OTHER = 3;
    private int collectId = INVALID_ID;
    private String title;
    private String author;
    private String link;
    private int deleteId;
    private int originId = INVALID_ID;
    private boolean collect;
    private int fromType;

    private CollectInfo() {

    }

    public int getCollectId() {
        return collectId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public int getDeleteId() {
        return deleteId;
    }

    public int getOriginId() {
        return originId;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDeleteId(int deleteId) {
        this.deleteId = deleteId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public CollectInfo(CollectInfo collectInfo) {
        collectId = collectInfo.collectId;
        collect = collectInfo.collect;
        title = collectInfo.title;
        author = collectInfo.author;
        link = collectInfo.link;
        deleteId = collectInfo.deleteId;
        originId = collectInfo.originId;
        fromType = collectInfo.fromType;
    }

    protected CollectInfo(Parcel in) {
        collectId = in.readInt();
        title = in.readString();
        author = in.readString();
        link = in.readString();
        deleteId = in.readInt();
        originId = in.readInt();
        collect = in.readByte() != 0;
        fromType = in.readInt();
    }

    public static class Builder {
        private CollectInfo collectInfo = new CollectInfo();

        public Builder collectId(int collectId) {
            collectInfo.collectId = collectId;
            return this;
        }

        public Builder title(String title) {
            collectInfo.title = title;
            return this;
        }

        public Builder author(String author) {
            collectInfo.author = author;
            return this;
        }

        public Builder link(String link) {
            collectInfo.link = link;
            return this;
        }

        public Builder deleteId(int deleteId) {
            collectInfo.deleteId = deleteId;
            return this;
        }

        public Builder originId(int originId) {
            collectInfo.originId = originId;
            return this;
        }

        public Builder collect(boolean collect) {
            collectInfo.collect = collect;
            return this;
        }

        public Builder fromType(int fromType) {
            collectInfo.fromType = fromType;
            return this;
        }

        public CollectInfo build() {
            if (collectInfo.collectId == INVALID_ID) {
                collectInfo.collectId = CollectInfo.createCollectIdByStr(
                        collectInfo.title, collectInfo.author, collectInfo.link
                );
            }
            return collectInfo;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(collectId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(link);
        dest.writeInt(deleteId);
        dest.writeInt(originId);
        dest.writeByte((byte) (collect ? 1 : 0));
        dest.writeInt(fromType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectInfo> CREATOR = new Creator<CollectInfo>() {
        @Override
        public CollectInfo createFromParcel(Parcel in) {
            return new CollectInfo(in);
        }

        @Override
        public CollectInfo[] newArray(int size) {
            return new CollectInfo[size];
        }
    };

    private static int createCollectIdByStr(String title, String author, String link) {
        int collectId = title == null ? 0 : title.hashCode();
        collectId = collectId + (author == null ? 0 : author.hashCode());
        collectId = collectId + (link == null ? 0 : link.hashCode());
        return collectId == 0 ? INVALID_ID : collectId;
    }
}
