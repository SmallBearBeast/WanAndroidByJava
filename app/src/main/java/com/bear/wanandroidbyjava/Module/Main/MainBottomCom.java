package com.bear.wanandroidbyjava.Module.Main;

import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.Module.Home.HomeListCom;
import com.bear.wanandroidbyjava.Module.Project.ProjectCom;
import com.bear.wanandroidbyjava.Module.Public.PublicCom;
import com.bear.wanandroidbyjava.Module.System.SystemCom;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;
import com.example.libframework.Wrapper.OnPageChangeListenerWrapper;

public class MainBottomCom extends ViewComponent<ComponentAct> implements View.OnClickListener {
    private static final long CLICK_INTERVAL = 500;
    private int mLastClickViewId;
    private long mLastClickTs;
    private ImageView[] mTabIconIvs = new ImageView[5];

    @Override
    protected void onCreate() {
        super.onCreate();
        ViewPager mViewPager = findViewById(R.id.mainViewPager);
        int[] viewIds = new int[]{
                R.id.iv_home_tab_icon, R.id.iv_system_tab_icon, R.id.iv_public_tab_icon, R.id.iv_project_tab_icon, R.id.iv_personal_tab_icon
        };
        for (int i = 0; i < viewIds.length; i++) {
            mTabIconIvs[i] = findViewById(viewIds[i]);
        }
        mTabIconIvs[0].setColorFilter(ResourceUtil.getColor(R.color.color_03a9f4));
        mViewPager.addOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageSelected(int position) {
                for (ImageView imageView : mTabIconIvs) {
                    imageView.setColorFilter(ResourceUtil.getColor(R.color.color_5c5c5c));
                }
                mTabIconIvs[position].setColorFilter(ResourceUtil.getColor(R.color.color_03a9f4));
            }
        });
        clickListener(this, R.id.ll_home_tab, R.id.ll_system_tab, R.id.ll_public_tab, R.id.ll_project_tab, R.id.ll_personal_tab);
    }

    @Override
    public void onClick(View v) {
        long curTs = System.currentTimeMillis();
        if (mLastClickViewId != v.getId() || curTs - mLastClickTs > CLICK_INTERVAL) {
            switch (v.getId()) {
                case R.id.ll_home_tab:
                    ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_HOME);
                    break;
                case R.id.ll_system_tab:
                    ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_SYSTEM);
                    break;
                case R.id.ll_public_tab:
                    ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PUBLIC);
                    break;
                case R.id.ll_project_tab:
                    ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PROJECT);
                    break;
                case R.id.ll_personal_tab:
                    ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PERSONAL);
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.ll_home_tab:
                    ComponentService.get().getComponent(HomeListCom.class).scrollToTop();
                    break;
                case R.id.ll_system_tab:
                    ComponentService.get().getComponent(SystemCom.class).scrollToTop();
                    break;
                case R.id.ll_public_tab:
                    ComponentService.get().getComponent(PublicCom.class).scrollToTop();
                    break;
                case R.id.ll_project_tab:
                    ComponentService.get().getComponent(ProjectCom.class).scrollToTop();
                    break;
            }
        }
        mLastClickViewId = v.getId();
        mLastClickTs = curTs;
    }
}
