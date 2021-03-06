package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Module.Collect.CollectArticleView;
import com.bear.wanandroidbyjava.Module.Web.WebAct;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.StringUtil;

public class HomeListVHBridge extends VHBridge<HomeListVHBridge.HomeVHolder> {

    @NonNull
    @Override
    protected HomeVHolder onCreateViewHolder(@NonNull View view) {
        return new HomeVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_article;
    }

    static class HomeVHolder extends VHolder<Article> {
        private TextView mTvNewMark;
        private TextView mTvAuthorName;
        private TextView mTvTagName;
        private TextView mTvDate;
        private TextView mTvArticleName;
        private TextView mTvArticleDesc;
        private TextView mTvTopMark;
        private TextView mTvChapterName;
        private CollectArticleView collectArticleView;

        public HomeVHolder(View itemView) {
            super(itemView);
            mTvNewMark = findViewById(R.id.tv_new_mark);
            mTvAuthorName = findViewById(R.id.tv_author_name);
            mTvTagName = findViewById(R.id.tv_tag_name);
            mTvDate = findViewById(R.id.tv_date);
            mTvArticleName = findViewById(R.id.tv_article_title);
            mTvArticleDesc = findViewById(R.id.tv_article_desc);
            mTvTopMark = findViewById(R.id.tv_top_mark);
            mTvChapterName = findViewById(R.id.tv_chapter_name);
            collectArticleView = findViewById(R.id.collectArticleView);
            setOnClickListener(itemView);
        }

        @Override
        public void bindFull(int pos, Article article) {
            super.bindFull(pos, article);
            mTvNewMark.setVisibility(article.fresh ? View.VISIBLE : View.GONE);
            mTvAuthorName.setText(article.author);
            mTvTagName.setText(article.tag);
            mTvDate.setText(article.dateStr);
            mTvArticleName.setText(article.title);
            if (StringUtil.isEmpty(article.description)) {
                mTvArticleDesc.setVisibility(View.GONE);
            } else {
                mTvArticleDesc.setText(article.description);
            }
            mTvTopMark.setVisibility(article.top ? View.VISIBLE : View.GONE);
            mTvChapterName.setText(article.superChapterName + "." + article.chapterName);
            collectArticleView.setCollectInfo(article.toCollectInfo());
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.cl_item_container) {
                Article article = getData();
                WebAct.go(getContext(), article);
            }
        }
    }
}
