package com.bear.wanandroidbyjava.Module.Main;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.component.ActivityComponent;
import com.bear.libcomponent.component.ComponentService;
import com.bear.wanandroidbyjava.Module.Home.HomeListCom;
import com.bear.wanandroidbyjava.Module.Project.ProjectCom;
import com.bear.wanandroidbyjava.Module.Public.PublicCom;
import com.bear.wanandroidbyjava.Module.System.SystemCom;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.ItemView;
import com.example.libbase.Util.ResourceUtil;
import com.example.libframework.Wrapper.OnPageChangeListenerWrapper;

public class MainBottomCom extends ActivityComponent implements View.OnClickListener {
    private static final long CLICK_INTERVAL = 500;
    private int lastClickViewId;
    private long lastClickTs;
    private final ItemView[] itemViews = new ItemView[5];

    @Override
    protected void onCreate() {
        super.onCreate();
        ViewPager mViewPager = findViewById(R.id.mainViewPager);
        int[] viewIds = new int[]{
                R.id.homeTabItemView, R.id.systemTabItemView, R.id.publicTabItemView, R.id.projectTabItemView, R.id.personalTabItemView
        };
        for (int i = 0; i < viewIds.length; i++) {
            itemViews[i] = (ItemView) findViewAndSetListener(this, viewIds[i]);
        }
        itemViews[0].setIconTint(ResourceUtil.getColor(R.color.color_03A9F4));
        mViewPager.addOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageSelected(int position) {
                for (ItemView itemView : itemViews) {
                    itemView.setIconTint(ResourceUtil.getColor(R.color.color_5C5C5C));
                }
                itemViews[position].setIconTint(ResourceUtil.getColor(R.color.color_03A9F4));
            }
        });
    }

    @Override
    public void onClick(View v) {
        long curTs = System.currentTimeMillis();
        int viewId = v.getId();
        if (lastClickViewId != v.getId() || curTs - lastClickTs > CLICK_INTERVAL) {
            if (viewId == R.id.homeTabItemView) {
                ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_HOME);
            } else if (viewId == R.id.systemTabItemView) {
                ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_SYSTEM);
            } else if (viewId == R.id.publicTabItemView) {
                ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PUBLIC);
            } else if (viewId == R.id.projectTabItemView) {
                ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PROJECT);
            } else if (viewId == R.id.personalTabItemView) {
                ComponentService.get().getComponent(MainContentCom.class).switchTab(MainContentCom.TAB_PERSONAL);
            }
        } else {
            if (viewId == R.id.homeTabItemView) {
                ComponentService.get().getComponent(HomeListCom.class).scrollToTop();
            } else if (viewId == R.id.systemTabItemView) {
                ComponentService.get().getComponent(SystemCom.class).scrollToTop();
            } else if (viewId == R.id.publicTabItemView) {
                ComponentService.get().getComponent(PublicCom.class).scrollToTop();
            } else if (viewId == R.id.projectTabItemView) {
                ComponentService.get().getComponent(ProjectCom.class).scrollToTop();
            }
        }
        lastClickViewId = v.getId();
        lastClickTs = curTs;
    }
}
