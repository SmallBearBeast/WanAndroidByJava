package com.bear.wanandroidbyjava.Module.Public;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Bean.PublicTab;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;
import com.example.libframework.CoreUI.ComponentAct;
import com.example.libframework.Rv.VHBridge;
import com.example.libframework.Rv.VHolder;

public class PublicTabVHBridge extends VHBridge<PublicTabVHBridge.TabVHolder> {
    private int mSelectTabPos = 0;

    @NonNull
    @Override
    protected TabVHolder onCreateViewHolder(@NonNull View view) {
        return new TabVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_public_tab;
    }

    class TabVHolder extends VHolder<PublicTab> {
        private TextView mTvPublicName;

        public TabVHolder(View itemView) {
            super(itemView);
            mTvPublicName = findViewById(R.id.tv_public_name);
            setOnClickListener(itemView);
        }

        @Override
        public void bindFull(int pos, PublicTab publicTab) {
            super.bindFull(pos, publicTab);
            itemView.setBackgroundColor(mSelectTabPos == pos ? ResourceUtil.getColor(R.color.color_aaaaaa) : Color.TRANSPARENT);
            mTvPublicName.setText(publicTab.name);
        }

        @Override
        public void onClick(View v) {
            if (mContext instanceof ComponentAct && mSelectTabPos != mPos) {
                ((ComponentAct)mContext).getComponent(PublicCom.class).switchTabArticle(mData.id);
                mDataManager.update(mSelectTabPos);
                mDataManager.update(mPos);
                mSelectTabPos = mPos;
            }
        }
    }
}
