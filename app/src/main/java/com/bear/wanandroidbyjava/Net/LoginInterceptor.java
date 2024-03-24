package com.bear.wanandroidbyjava.Net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        WanResponce<String> wanResponce = toObj(response.body().string(), WanTypeToken.STRING_TOKEN);
        return response;
    }

    private <T> T toObj(String json, TypeToken<T> token){
        if(token.getType() == String.class) {
            return (T) json;
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(json, token.getType());
    }
}
