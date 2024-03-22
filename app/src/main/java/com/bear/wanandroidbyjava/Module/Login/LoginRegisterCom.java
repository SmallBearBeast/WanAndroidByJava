package com.bear.wanandroidbyjava.Module.Login;

import android.view.View;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.component.ActivityComponent;
import com.bear.wanandroidbyjava.R;

public class LoginRegisterCom extends ActivityComponent implements View.OnClickListener {
    private ViewPager loginRegisterViewPager;
    @Override
    protected void onCreate() {
        setOnClickListener(this, R.id.closeIv);
        loginRegisterViewPager = findViewById(R.id.loginRegisterViewPager);
        loginRegisterViewPager.setAdapter(new LoginRegisterAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.closeIv) {
            getActivity().finish();
        }
    }

    public void goToLoginPage() {
        loginRegisterViewPager.setCurrentItem(0, true);
    }

    public void goToRegisterPage() {
        loginRegisterViewPager.setCurrentItem(1, true);
    }
}
