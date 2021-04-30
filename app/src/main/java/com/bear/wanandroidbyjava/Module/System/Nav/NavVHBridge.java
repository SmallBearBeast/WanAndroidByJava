package com.bear.wanandroidbyjava.Module.System.Nav;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Data.Bean.Article;
import com.bear.wanandroidbyjava.Data.Bean.Nav;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.FlowLayout;
import com.example.libbase.Util.ToastUtil;

public class NavVHBridge extends VHBridge<NavVHBridge.NavVHolder> {
    @NonNull
    @Override
    protected NavVHolder onCreateViewHolder(@NonNull View view) {
        return new NavVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.view_tree_nav_item;
    }

    static class NavVHolder extends VHolder<Nav> {
        private final TextView treeNavTitleTv;
        private final FlowLayout treeNavLayout;

        public NavVHolder(View itemView) {
            super(itemView);
            treeNavTitleTv = findViewById(R.id.treeNavTitleTv);
            treeNavLayout = findViewById(R.id.treeNavLayout);
            treeNavLayout.setFlowClickListener(new FlowLayout.OnFlowClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() instanceof Article) {
                        Article article = (Article) view.getTag();
                        ToastUtil.showToast(article.toString());
                    }
                }
            });
        }

        @Override
        public void bindFull(int pos, Nav nav) {
            super.bindFull(pos, nav);
            treeNavTitleTv.setText(nav.name);
            Object tag = treeNavLayout.getTag();
            if (tag == null || !tag.equals(pos)) {
                treeNavLayout.removeAllViews();
                for (Article article : nav.articleList) {
                    treeNavLayout.addView(createFlowView(article, treeNavLayout));
                }
            }
            treeNavLayout.setTag(pos);

        }

        private View createFlowView(Article article, FlowLayout flowLayout) {
            View flowView = LayoutInflater.from(getContext()).inflate(R.layout.view_tree_nav_sub_item, flowLayout, false);
            TextView tv = flowView.findViewById(R.id.subTreeNavTextTv);
            tv.setText(article.title);
            flowView.setTag(article);
            return flowView;
        }
    }
}
