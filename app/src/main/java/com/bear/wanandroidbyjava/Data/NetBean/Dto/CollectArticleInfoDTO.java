package com.bear.wanandroidbyjava.Data.NetBean.Dto;

import com.bear.wanandroidbyjava.Data.Bean.CollectArticleInfoBean;
import com.google.gson.annotations.SerializedName;

public class CollectArticleInfoDTO {
    @SerializedName("count")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CollectArticleInfoBean toCollectArticleInfo() {
        CollectArticleInfoBean collectArticleInfo = new CollectArticleInfoBean();
        collectArticleInfo.setCount(count != null ? count : 0);
        return collectArticleInfo;
    }
}
