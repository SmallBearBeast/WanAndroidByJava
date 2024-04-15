package com.bear.wanandroidbyjava.Module.Personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.bear.libcomponent.component.FragmentComponent;
import com.bear.wanandroidbyjava.Data.Bean.UserDataBean;
import com.bear.wanandroidbyjava.Module.Login.LoginRegisterAct;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Util.OtherUtil;
import com.bear.wanandroidbyjava.WanConstant;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.ViewUtil;

public class PersonalInfoCom extends FragmentComponent implements View.OnClickListener {

    private ImageView mIvUserBackground;
    private ImageView userAvatarIv;
    private TextView goToLoginTv;
    private TextView mTvUserName;
    private TextView mTvUserId;
    private TextView mTvUserRank;
    private TextView mTvUserGrade;

    private PersonalVM personalVM;

    private ActivityResultLauncher<Intent> launcher;

    public PersonalInfoCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        launcher = getFragment().registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                UserDataBean userDataBean = (UserDataBean) result.getData().getSerializableExtra(WanConstant.BUNDLE_KEY_USER_DATA);
                updateInfoByLoginState(true);
                updateInfoByUserData(userDataBean);
                getComponent(PersonalItemCom.class).updateItemByLoginStateExt(true);
                getComponent(PersonalItemCom.class).updateItemByUserDataExt(userDataBean);
            }
        });
    }


    @Override
    protected void onCreateView() {
        initView();
        initViewModel();
        personalVM.fetchUserData();
    }

    private void initView() {
        mIvUserBackground = findViewById(R.id.userBgIv);
        mTvUserName = findViewById(R.id.userNameTv);
        goToLoginTv = findViewById(R.id.goToLoginTv);
        userAvatarIv = findViewById(R.id.userAvatarIv);
        mTvUserId = findViewById(R.id.userIdTv);
        mTvUserRank = findViewById(R.id.userRankTv);
        mTvUserGrade = findViewById(R.id.userGradeTv);
        setOnClickListener(this, R.id.goToLoginTv, R.id.userAvatarIv);

        updateInfoByLoginState(false);
    }

    private void initViewModel() {
        personalVM = new ViewModelProvider(getFragment()).get(PersonalVM.class);
        personalVM.getLogoutResultLD().observe(getFragment(), logoutResult -> {
            if (logoutResult) {
                updateInfoByLoginState(false);
            }
        });
        personalVM.getLoginStateLD().observe(getFragment(), this::updateInfoByLoginState);
        personalVM.getFetchUserDataResultLD().observe(getFragment(), userData -> {
            if (userData != null) {
                updateInfoByLoginState(true);
                updateInfoByUserData(userData);
            }
        });
    }

    private void updateInfoByUserData(UserDataBean userData) {
        if (userData != null) {
            mTvUserName.setText(userData.getUserInfoBean().getUsername());
            String idStr = ResourceUtil.getString(R.string.str_personal_id, userData.getUserInfoBean().getId());
            mTvUserId.setText(idStr);
            String gradeStr = ResourceUtil.getString(R.string.str_personal_grade, userData.getCoinInfoBean().getLevel());
            mTvUserGrade.setText(gradeStr);
            String rankStr = ResourceUtil.getString(R.string.str_personal_rank, userData.getCoinInfoBean().getRank());
            mTvUserRank.setText(rankStr);
        }
    }

    private void updateInfoByLoginState(boolean loginState) {
        if (loginState) {
            ViewUtil.visible(mTvUserName, mTvUserId, mTvUserGrade, mTvUserRank);
            ViewUtil.gone(goToLoginTv);
            userAvatarIv.setImageResource(R.drawable.ic_logo);
            userAvatarIv.setOnClickListener(null);
        } else {
            ViewUtil.invisible(mTvUserName, mTvUserId, mTvUserGrade, mTvUserRank);
            ViewUtil.visible(goToLoginTv);
            userAvatarIv.setImageDrawable(null);
            userAvatarIv.setOnClickListener(this);
        }
    }

    @Override
    protected void onDestroyView() {
        OtherUtil.clear(mIvUserBackground, userAvatarIv, mTvUserName, mTvUserId, mTvUserRank, mTvUserGrade);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userAvatarIv:
            case R.id.goToLoginTv:
                launcher.launch(new Intent(getContext(), LoginRegisterAct.class));
                break;
        }
    }
}
