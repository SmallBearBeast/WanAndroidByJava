package com.bear.wanandroidbyjava.Module.Login;

import android.view.View;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;

public class LoginRegisterCom extends ViewComponent<LoginRegisterAct> implements View.OnClickListener, ILoginRegisterCom {
    private ViewPager loginRegisterViewPager;
    @Override
    protected void onCreate() {
        clickListener(this, R.id.closeIv);
        loginRegisterViewPager = findViewById(R.id.loginRegisterViewPager);
        loginRegisterViewPager.setAdapter(new LoginRegisterAdapter(getDependence().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.closeIv) {
            getDependence().finish();
        }
    }


    @Override
    public void goToLoginPage() {
        loginRegisterViewPager.setCurrentItem(0, true);
    }

    @Override
    public void goToRegisterPage() {
        loginRegisterViewPager.setCurrentItem(1, true);
    }
}
