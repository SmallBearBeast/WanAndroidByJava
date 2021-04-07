package com.bear.wanandroidbyjava.Module.Public;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bear.libcomponent.ComponentAct;
import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Data.Bean.PublicTab;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;

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
            if (getContext() instanceof ComponentAct && mSelectTabPos != getPos()) {
                ((ComponentAct)getContext()).getComponent(PublicCom.class).switchTabArticle(getData().publicTabId);
                getDataManager().update(mSelectTabPos);
                getDataManager().update(getPos());
                mSelectTabPos = getPos();
            }
        }
    }
}
