package com.bear.wanandroidbyjava.Module.Collect;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bear.wanandroidbyjava.EventKey;
import com.bear.wanandroidbyjava.Manager.CollectManager;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Storage.CollectInfoStorage;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;
import com.example.libframework.Bus.Bus;
import com.example.libframework.Bus.Event;

public class CollectArticleView extends AppCompatImageView {
    private final CollectManager collectManager = new CollectManager();
    private CollectInfo collectInfo;

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
                if (collectInfo != null) {
                    if (collectInfo.getFromType() == CollectInfo.TYPE_ARTICLE) {
                        if (collectInfo.isCollect()) {
                            unCollect();
                        } else {
                            collect();
                        }
                    } else {
                        if (collectInfo.isCollect()) {
                            unCollectOut();
                        } else {
                            collectOut();
                        }
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
        final int articleId = collectInfo.getCollectId();
        final CollectInfo info = new CollectInfo(collectInfo);
        collectManager.collectArticle(articleId, new CollectManager.CollectListener() {
            @Override
            public void onCollect(boolean success) {
                if (articleId == collectInfo.getCollectId()) {
                    setCollect(success);
                }
                info.setCollect(success);
                postCollectEvent(info);
                CollectInfoStorage.saveCollectInfo(info);
            }
        });
    }

    private void unCollect() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
        }
        setCollect(false);
        final int articleId = collectInfo.getCollectId();
        final CollectInfo info = new CollectInfo(collectInfo);
        collectManager.unCollectArticle(articleId, new CollectManager.UnCollectListener() {
            @Override
            public void onUnCollect(boolean success) {
                if (articleId == collectInfo.getCollectId()) {
                    setCollect(!success);
                }
                info.setCollect(!success);
                postCollectEvent(info);
                CollectInfoStorage.saveCollectInfo(info);
            }
        });
    }

    private void collectOut() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
        }
        setCollect(true);
        final String title = collectInfo.getTitle();
        final String author = collectInfo.getAuthor();
        final String link = collectInfo.getLink();
        final CollectInfo info = new CollectInfo(collectInfo);
        collectManager.collectOutArticle(title, author, link, new CollectManager.OutCollectListener() {
            @Override
            public void onOutCollect(boolean success, CollectInfo tempCollectInfo) {
                if (StringUtil.equals(title, collectInfo.getTitle())
                        && StringUtil.equals(author, collectInfo.getAuthor())
                        && StringUtil.equals(link, collectInfo.getLink())) {
                    setCollect(success);
                    setDeleteIdAndOriginId(tempCollectInfo.getDeleteId(), tempCollectInfo.getOriginId());
                }
                info.setCollect(success);
                info.setDeleteId(tempCollectInfo.getDeleteId());
                info.setOriginId(tempCollectInfo.getOriginId());
                postOutCollectEvent(info);
                CollectInfoStorage.saveCollectInfo(info);
            }
        });
    }

    private void unCollectOut() {
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
        }
        setImageResource(R.drawable.ic_un_collect);
        final int id = collectInfo.getDeleteId();
        final int originId = collectInfo.getOriginId();
        final CollectInfo info = new CollectInfo(collectInfo);
        collectManager.unCollectOutArticleUrl(id, originId, new CollectManager.UnCollectListener() {
            @Override
            public void onUnCollect(boolean success) {
                if (id == collectInfo.getDeleteId() && originId == collectInfo.getOriginId()) {
                    setCollect(!success);
                }
                info.setCollect(!success);
                postOutCollectEvent(info);
                CollectInfoStorage.saveCollectInfo(info);
            }
        });
    }

    private void setCollect(boolean collect) {
        collectInfo.setCollect(collect);
        setImageResource(collect ? R.drawable.ic_collect : R.drawable.ic_un_collect);
    }

    private void setDeleteIdAndOriginId(int deleteId, int originId) {
        collectInfo.setDeleteId(deleteId);
        collectInfo.setOriginId(originId);
    }

    private void postCollectEvent(CollectInfo info) {
        Bus.get().post(new Event(EventKey.KEY_COLLECT_OR_UN_COLLECT_EVENT, info));
    }

    private void postOutCollectEvent(CollectInfo info) {
        Bus.get().post(new Event(EventKey.KEY_COLLECT_OR_UN_COLLECT_OUT_EVENT, info));
    }

    public void setCollectInfo(@NonNull CollectInfo collectInfo) {
        this.collectInfo = collectInfo;
        setCollect(collectInfo.isCollect());
    }
}
