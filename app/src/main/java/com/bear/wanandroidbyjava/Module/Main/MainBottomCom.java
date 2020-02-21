package com.bear.wanandroidbyjava.Module.Main;

import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.bear.wanandroidbyjava.Module.Home.HomeListCom;
import com.bear.wanandroidbyjava.Module.Project.ProjectCom;
import com.bear.wanandroidbyjava.Module.Public.PublicCom;
import com.bear.wanandroidbyjava.Module.System.SystemCom;
import com.bear.wanandroidbyjava.R;
import com.example.libbase.Util.ResourceUtil;
import com.example.libframework.CoreUI.ActComponent;
import com.example.libframework.Wrapper.OnPageChangeListenerWrapper;

public class MainBottomCom extends ActComponent {
    private int mLastClickViewId;
    private long mLastClickTs;
    private static final int TWO_CLICK_INTERVAL = 500;
    private ViewPager mViewPager;
    private ImageView[] mImageViews = new ImageView[5];

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPager = findViewById(R.id.vp_main_container);
        int[] viewIds = new int[]{
                R.id.iv_home, R.id.iv_system, R.id.iv_public, R.id.iv_project, R.id.iv_personal
        };
        for (int i = 0; i < viewIds.length; i++) {
            mImageViews[i] = findViewById(viewIds[i]);
        }
        mImageViews[0].setColorFilter(ResourceUtil.getColor(R.color.color_03a9f4));
        mViewPager.addOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImageViews.length; i++) {
                    mImageViews[i].setColorFilter(ResourceUtil.getColor(R.color.color_5c5c5c));
                }
                mImageViews[position].setColorFilter(ResourceUtil.getColor(R.color.color_03a9f4));
            }
        });
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long curTs = System.currentTimeMillis();
                if (mLastClickViewId != v.getId() || curTs - mLastClickTs > TWO_CLICK_INTERVAL) {
                    switch (v.getId()) {
                        case R.id.ll_home:
                            mMain.getComponent(MainContentCom.class).switchTab(0);
                            break;
                        case R.id.ll_system:
                            mMain.getComponent(MainContentCom.class).switchTab(1);
                            break;
                        case R.id.ll_public:
                            mMain.getComponent(MainContentCom.class).switchTab(2);
                            break;
                        case R.id.ll_project:
                            mMain.getComponent(MainContentCom.class).switchTab(3);
                            break;
                        case R.id.ll_personal:
                            mMain.getComponent(MainContentCom.class).switchTab(4);
                            break;
                    }
                } else {
                    switch (v.getId()) {
                        case R.id.ll_home:
                            mMain.getComponent(HomeListCom.class).scrollToTop();
                            break;
                        case R.id.ll_system:
                            mMain.getComponent(SystemCom.class).scrollToTop();
                            break;
                        case R.id.ll_public:
                            mMain.getComponent(PublicCom.class).scrollToTop();
                            break;
                        case R.id.ll_project:
                            mMain.getComponent(ProjectCom.class).scrollToTop();
                            break;
                    }
                }
                mLastClickViewId = v.getId();
                mLastClickTs = curTs;
            }
        }, R.id.ll_home, R.id.ll_system, R.id.ll_public, R.id.ll_project, R.id.ll_personal);
    }
}
