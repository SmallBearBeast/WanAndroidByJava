package com.bear.wanandroidbyjava.Module.Personal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bear.libcomponent.ComponentFrag;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;


public class PersonalCom extends ViewComponent<ComponentFrag> implements View.OnClickListener {
    private ImageView mIvUserBackground;
    private ImageView mIvUserIcon;
    private TextView mTvUserName;
    private TextView mTvUserId;
    private TextView mTvUserRank;
    private TextView mTvUserGrade;

    @Override
    protected void onCreateView() {
        mIvUserBackground = findViewById(R.id.iv_user_background);
        mIvUserIcon = findViewById(R.id.iv_user_icon);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvUserId = findViewById(R.id.tv_user_id);
        mTvUserRank = findViewById(R.id.tv_user_rank);
        mTvUserGrade = findViewById(R.id.tv_user_grade);
        clickListener(this, R.id.ll_my_integral, R.id.ll_my_share, R.id.ll_my_collect, R.id.ll_my_todo, R.id.ll_about_developer, R.id.ll_my_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_integral:
                break;
            case R.id.ll_my_share:
                break;
            case R.id.ll_my_collect:
                break;
            case R.id.ll_my_todo:
                break;
            case R.id.ll_about_developer:
                break;
            case R.id.ll_my_setting:
                break;
        }
    }

    @Override
    protected void onDestroyView() {
        mIvUserBackground = null;
        mIvUserIcon = null;
        mTvUserName = null;
        mTvUserId = null;
        mTvUserRank = null;
        mTvUserGrade = null;
    }
}
