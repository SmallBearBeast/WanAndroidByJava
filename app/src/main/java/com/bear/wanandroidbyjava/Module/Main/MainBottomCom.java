package com.bear.wanandroidbyjava.Module.Main;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.bear.libcomponent.ComponentAct;
import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.Module.Home.IHomeListCom;
import com.bear.wanandroidbyjava.Module.Project.IProjectCom;
import com.bear.wanandroidbyjava.Module.Public.IPublicCom;
import com.bear.wanandroidbyjava.Module.System.ISystemCom;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.ItemView;
import com.example.libbase.Util.ResourceUtil;
import com.example.libframework.Wrapper.OnPageChangeListenerWrapper;

public class MainBottomCom extends ViewComponent<ComponentAct> implements View.OnClickListener {
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
            itemViews[i] = clickListenerAndView(this, viewIds[i]);
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
                ComponentService.get().getComponent(IMainContentCom.class).switchTab(MainContentCom.TAB_HOME);
            } else if (viewId == R.id.systemTabItemView) {
                ComponentService.get().getComponent(IMainContentCom.class).switchTab(MainContentCom.TAB_SYSTEM);
            } else if (viewId == R.id.publicTabItemView) {
                ComponentService.get().getComponent(IMainContentCom.class).switchTab(MainContentCom.TAB_PUBLIC);
            } else if (viewId == R.id.projectTabItemView) {
                ComponentService.get().getComponent(IMainContentCom.class).switchTab(MainContentCom.TAB_PROJECT);
            } else if (viewId == R.id.personalTabItemView) {
                ComponentService.get().getComponent(IMainContentCom.class).switchTab(MainContentCom.TAB_PERSONAL);
            }
        } else {
            if (viewId == R.id.homeTabItemView) {
                ComponentService.get().getComponent(IHomeListCom.class).scrollToTop();
            } else if (viewId == R.id.systemTabItemView) {
                ComponentService.get().getComponent(ISystemCom.class).scrollToTop();
            } else if (viewId == R.id.publicTabItemView) {
                ComponentService.get().getComponent(IPublicCom.class).scrollToTop();
            } else if (viewId == R.id.projectTabItemView) {
                ComponentService.get().getComponent(IProjectCom.class).scrollToTop();
            }
        }
        lastClickViewId = v.getId();
        lastClickTs = curTs;
    }
}
