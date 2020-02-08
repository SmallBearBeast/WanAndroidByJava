package com.bear.wanandroidbyjava.Module.Public;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;
import com.example.libframework.CoreUI.ComponentFrag;

public class PublicListFrag extends ComponentFrag {
    private static final String TAB_ID = "tab_id";
    private int mTabId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regComponent(new PublicListCom(mTabId), mTabId);
    }

    @Override
    protected void handleArgument(@NonNull Bundle bundle) {
        mTabId = bundle.getInt(TAB_ID);
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_tab_article;
    }

    public static PublicListFrag newInstance(int tabId) {
        PublicListFrag publicListFrag = new PublicListFrag();
        Bundle argument = new Bundle();
        argument.putInt(TAB_ID, tabId);
        publicListFrag.setArguments(argument);
        return publicListFrag;
    }
}
