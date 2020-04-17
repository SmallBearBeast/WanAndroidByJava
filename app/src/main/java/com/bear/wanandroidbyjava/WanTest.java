package com.bear.wanandroidbyjava;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WanTest {
    private static Pattern S_PATTERN_1 = Pattern.compile("^(http|https)(://)([A-Za-z0-9.]++$|[A-Za-z0-9.]++[/?].*+)");
    public static void main(String[] args) {
        System.out.println("http://baidu.com = " + isValidUrl("http://baidu.com&"));
        System.out.println("http://baidu.com/ = " + isValidUrl("http://baidu.com&/"));
        System.out.println("http://baidu.com?a=1 = " + isValidUrl("http://baidu.&com?a=1"));
        System.out.println("http://baidu.com/a/b?a=1&b=1 = " + isValidUrl("http://baidu.&com/a/b?a=1&b=1"));
    }

    private static boolean isValidUrl(String url) {
        Matcher mat = S_PATTERN_1.matcher(url.trim());
        return mat.matches();
    }
}
