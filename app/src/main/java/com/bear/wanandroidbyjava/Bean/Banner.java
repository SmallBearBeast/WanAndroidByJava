package com.bear.wanandroidbyjava.Bean;

import java.util.List;

public class Banner {
    public List<String> imageUrlList;
    public List<String> clickUrlList;

    @Override
    public String toString() {
        return "Banner{" +
                "imageUrlList=" + imageUrlList +
                ", clickUrlList=" + clickUrlList +
                '}';
    }
}
