package com.bear.wanandroidbyjava.Module.Home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;
import com.bear.wanandroidbyjava.Data.Bean.BannerSet;
import com.bear.wanandroidbyjava.Module.Web.WebAct;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.LoopViewPager.LoopViewPager;
import com.bumptech.glide.Glide;
import com.example.libbase.OnProtectClickListener;

public class BannerVHBridge extends VHBridge<BannerVHBridge.BannerVHolder> {

    @NonNull
    @Override
    protected BannerVHolder onCreateViewHolder(@NonNull View view) {
        return new BannerVHolder(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.item_banner;
    }

    @Override
    protected boolean isSupportLifecycle() {
        return true;
    }

    static class BannerVHolder extends VHolder<BannerSet> {
        private LoopViewPager mLvpBanner;

        public BannerVHolder(View itemView) {
            super(itemView);
            mLvpBanner = findViewById(R.id.lvp_banner);
        }

        @Override
        protected void onStart() {
            mLvpBanner.startLoop();
        }

        @Override
        protected void onStop() {
            mLvpBanner.stopLoop();
        }

        @Override
        public void bindFull(int pos, BannerSet bannerSet) {
            super.bindFull(pos, bannerSet);
            mLvpBanner.setAdapter(new BannerAdapter(bannerSet));
        }
    }

    static class BannerAdapter extends PagerAdapter {
        private BannerSet mBannerSet;

        public BannerAdapter(BannerSet bannerSet) {
            mBannerSet = bannerSet;
        }

        @Override
        public int getCount() {
            return mBannerSet.imageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            ImageView iv = new ImageView(container.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            container.addView(iv);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(iv).load(mBannerSet.imageUrlList.get(position)).into(iv);
            iv.setOnClickListener(new OnProtectClickListener() {
                @Override
                public void onProtectClick(View view) {
                    WebAct.go(view.getContext(), null, mBannerSet.clickUrlList.get(position));
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
