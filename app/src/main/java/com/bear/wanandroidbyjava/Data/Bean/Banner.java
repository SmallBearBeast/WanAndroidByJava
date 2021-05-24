package com.bear.wanandroidbyjava.Data.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {
    public String desc;
    public int id;
    public String imagePath;
    public int isVisible;
    public int order;
    public String title;
    public int type;
    public String url;

    public Banner() {

    }

    protected Banner(Parcel in) {
        desc = in.readString();
        id = in.readInt();
        imagePath = in.readString();
        isVisible = in.readInt();
        order = in.readInt();
        title = in.readString();
        type = in.readInt();
        url = in.readString();
    }

    public static final Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeInt(isVisible);
        dest.writeInt(order);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(url);
    }
}
