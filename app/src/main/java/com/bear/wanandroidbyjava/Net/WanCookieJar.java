package com.bear.wanandroidbyjava.Net;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Storage.CookieStorage;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class WanCookieJar implements CookieJar {
    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (NetUrl.REGISTER.equals(url.toString()) || NetUrl.LOGIN.equals(url.toString())) {
            CookieStorage.saveCookies(url.host(), cookies);
        } else if (NetUrl.LOGOUT.equals(url.toString())) {
            CookieStorage.clearCookies();
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return CookieStorage.getCookies(url.host());
    }
}
