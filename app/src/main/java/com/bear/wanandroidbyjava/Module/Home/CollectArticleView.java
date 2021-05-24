package com.bear.wanandroidbyjava.Module.Home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.Manager.CollectManager;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.ToastUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;

public class CollectArticleView extends AppCompatImageView {
    private final CollectManager collectManager = new CollectManager();
    private Article article;

    public CollectArticleView(Context context) {
        this(context, null);
    }

    public CollectArticleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.ic_un_collect);
        setImageTintMode(PorterDuff.Mode.SRC_IN);
        setImageTintList(ColorStateList.valueOf(ResourceUtil.getColor(R.color.color_FA3A3A)));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (article != null) {
                    if (article.collect) {
                        unCollect();
                    } else {
                        collect();
                    }
                }
            }
        });
    }

    private void collect() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
        }
        setCollect(true);
        final int articleId = article.articleId;
        collectManager.collectArticle(articleId, new CollectManager.CollectListener() {
            @Override
            public void onCollect(boolean success) {
                if (articleId == article.articleId) {
                    article.collect = success;
                    setCollect(success);
                    postBusEvent(success);
                }
            }
        });
    }

    private void unCollect() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
        }
        setImageResource(R.drawable.ic_un_collect);
        final int articleId = article.articleId;
        collectManager.unCollectArticle(articleId, new CollectManager.UnCollectListener() {
            @Override
            public void onUnCollect(boolean success) {
                if (articleId == article.articleId) {
                    article.collect = !success;
                    setCollect(!success);
                    postBusEvent(!success);
                }
            }
        });
    }

    private void setCollect(boolean collect) {
        setImageResource(collect ? R.drawable.ic_collect : R.drawable.ic_un_collect);
    }

    private void postBusEvent(boolean collect) {
        String collectKey = collect ? EventKey.KEY_COLLECT_ARTICLE : EventKey.KEY_UN_COLLECT_ARTICLE;
        Bus.get().post(new Event(collectKey, article));
    }

    public void setArticle(@NonNull Article article) {
        this.article = article;
        setCollect(article.collect);
    }
}
