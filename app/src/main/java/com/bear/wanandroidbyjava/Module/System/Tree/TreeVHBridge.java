package com.bear.wanandroidbyjava.Module.System.Tree;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Bean.SubTree;
import com.bear.wanandroidbyjava.Bean.Tree;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.FlowLayout;
import com.example.libbase.Util.ToastUtil;
import com.example.libbase.Util.XmlDrawableUtil;
import com.example.libframework.Rv.VHBridge;
import com.example.libframework.Rv.VHolder;

public class TreeVHBridge extends VHBridge<TreeVHBridge.TreeVHolder> {
    @NonNull
    @Override
    protected TreeVHolder onCreateViewHolder(@NonNull View view) {
        return new TreeVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_tree;
    }

    class TreeVHolder extends VHolder<Tree> {
        private TextView mTvTreeTitle;
        private FlowLayout mFlSubTreeContainer;

        public TreeVHolder(View itemView) {
            super(itemView);
            mTvTreeTitle = findViewById(R.id.tv_tree_name);
            mFlSubTreeContainer = findViewById(R.id.fl_sub_tree_container);
            mFlSubTreeContainer.setFlowClickListener(new FlowLayout.OnFlowClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() instanceof SubTree) {
                        SubTree subTree = (SubTree) view.getTag();
                        ToastUtil.showToast(subTree.toString());
                    }
                }
            });
        }

        @Override
        public void bindFull(int pos, Tree tree) {
            super.bindFull(pos, tree);
            mTvTreeTitle.setText(tree.name);
            Object tag = mFlSubTreeContainer.getTag();
            if (tag == null || !tag.equals(pos)) {
                mFlSubTreeContainer.removeAllViews();
                for (SubTree subTree : tree.subTreeList) {
                    mFlSubTreeContainer.addView(createFlowView(subTree));
                }
            }
            mFlSubTreeContainer.setTag(pos);

        }

        private View createFlowView(SubTree subTree) {
            View flowView = LayoutInflater.from(mContext).inflate(R.layout.layout_sub_tree_view, null);
            XmlDrawableUtil.slCRect(R.color.color_aaaaaa, R.color.color_5c5c5c, 100).setView(flowView);
            TextView tv = flowView.findViewById(R.id.tv_sub_tree_name);
            tv.setText(subTree.name);
            flowView.setTag(subTree);
            return flowView;
        }
    }
}
