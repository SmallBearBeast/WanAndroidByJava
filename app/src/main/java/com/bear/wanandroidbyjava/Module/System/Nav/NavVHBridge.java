package com.bear.wanandroidbyjava.Module.System.Nav;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Bean.Article;
import com.bear.wanandroidbyjava.Bean.Nav;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.FlowLayout;
import com.example.libbase.Util.ToastUtil;
import com.example.libbase.Util.XmlDrawableUtil;

public class NavVHBridge extends VHBridge<NavVHBridge.NavVHolder> {
    @NonNull
    @Override
    protected NavVHolder onCreateViewHolder(@NonNull View view) {
        return new NavVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_nav;
    }

    class NavVHolder extends VHolder<Nav> {
        private TextView mTvNavTitle;
        private FlowLayout mFlSubNavContainer;

        public NavVHolder(View itemView) {
            super(itemView);
            mTvNavTitle = findViewById(R.id.tv_nav_name);
            mFlSubNavContainer = findViewById(R.id.fl_sub_nav_container);
            mFlSubNavContainer.setFlowClickListener(new FlowLayout.OnFlowClickListener() {
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
            mTvNavTitle.setText(nav.name);
            Object tag = mFlSubNavContainer.getTag();
            if (tag == null || !tag.equals(pos)) {
                mFlSubNavContainer.removeAllViews();
                for (Article article : nav.articleList) {
                    mFlSubNavContainer.addView(createFlowView(article));
                }
            }
            mFlSubNavContainer.setTag(pos);

        }

        private View createFlowView(Article article) {
            View flowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sub_tree_view, null);
            XmlDrawableUtil.slRect(true, R.color.color_aaaaaa, R.color.color_5c5c5c, 100f).setView(flowView);
            TextView tv = flowView.findViewById(R.id.tv_sub_tree_name);
            tv.setText(article.title);
            flowView.setTag(article);
            return flowView;
        }
    }
}
