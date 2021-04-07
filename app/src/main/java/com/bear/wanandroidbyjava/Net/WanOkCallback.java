package com.bear.wanandroidbyjava.Net;

import com.example.libokhttp.OkCallback;
import com.google.gson.reflect.TypeToken;

public class WanOkCallback<T> extends OkCallback<WanResponce<T>> {
    public WanOkCallback(TypeToken<WanResponce<T>> typeToken) {
        super(typeToken);
    }
}
