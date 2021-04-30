package com.bear.wanandroidbyjava.Module.System.Tree;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Data.Bean.SubTree;
import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.FlowLayout;
import com.example.libbase.Util.ToastUtil;

public class TreeVHBridge extends VHBridge<TreeVHBridge.TreeVHolder> {
    @NonNull
    @Override
    protected TreeVHolder onCreateViewHolder(@NonNull View view) {
        return new TreeVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.view_tree_nav_item;
    }

    static class TreeVHolder extends VHolder<Tree> {
        private final TextView treeNavTitleTv;
        private final FlowLayout treeNavLayout;

        public TreeVHolder(View itemView) {
            super(itemView);
            treeNavTitleTv = findViewById(R.id.treeNavTitleTv);
            treeNavLayout = findViewById(R.id.treeNavLayout);
            treeNavLayout.setFlowClickListener(new FlowLayout.OnFlowClickListener() {
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
            treeNavTitleTv.setText(tree.name);
            Object tag = treeNavLayout.getTag();
            if (tag == null || !tag.equals(pos)) {
                treeNavLayout.removeAllViews();
                for (SubTree subTree : tree.subTreeList) {
                    treeNavLayout.addView(createFlowView(subTree, treeNavLayout));
                }
            }
            treeNavLayout.setTag(pos);

        }

        private View createFlowView(SubTree subTree, FlowLayout flowLayout) {
            View flowView = LayoutInflater.from(getContext()).inflate(R.layout.view_tree_nav_sub_item, flowLayout, false);
            TextView tv = flowView.findViewById(R.id.subTreeNavTextTv);
            tv.setText(subTree.name);
            flowView.setTag(subTree);
            return flowView;
        }
    }
}
