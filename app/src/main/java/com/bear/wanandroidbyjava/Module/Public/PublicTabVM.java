package com.bear.wanandroidbyjava.Module.Public;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.PublicTab;
import com.bear.wanandroidbyjava.Data.NetBean.PublicTabBean;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Storage.DataBase.WanRoomDataBase;
import com.example.libbase.Executor.BgThreadExecutor;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PublicTabVM extends ViewModel {
    private static final String TAG = "PublicTabVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<Pair<Boolean, List<PublicTab>>> mPublicTabPairLD = new MutableLiveData<>();
    private Comparator<PublicTab> mPublicTabComparator = new Comparator<PublicTab>() {
        @Override
        public int compare(PublicTab publicTab_1, PublicTab publicTab_2) {
            if (publicTab_1.order < publicTab_2.order) {
                return -1;
            } else if (publicTab_1.order > publicTab_2.order){
                return 1;
            } else {
                return 0;
            }
        }
    };

    public void fetchTab() {
        SLog.d(TAG, "fetchTab: start");
        mShowProgressLD.postValue(true);
        fetchTabFromDb();
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "fetchTab: net is unConnected");
            mShowProgressLD.postValue(false);
            return;
        }
        OkHelper.getInstance().getMethod(NetUrl.PUBLIC_TAB, new WanOkCallback<List<PublicTabBean>>(WanTypeToken.PUBLIC_TAB_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<PublicTabBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTab: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "fetchTab: publicTabList is empty");
                    } else {
                        List<PublicTab> publicTabList = new ArrayList<>();
                        List<PublicTabBean> publicTabBeanList = data.data;
                        for (PublicTabBean publicTabBean : publicTabBeanList) {
                            publicTabList.add(publicTabBean.toPublicTab());
                        }
                        Collections.sort(publicTabList, mPublicTabComparator);
                        mPublicTabPairLD.postValue(new Pair<>(true, publicTabList));
                        mIsFirstLoad = false;
                    }
                }
                mShowProgressLD.postValue(false);
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTab: onFail");
                mShowProgressLD.postValue(false);
            }
        });
    }

    public void savePublicTabList(final List<PublicTab> publicTabList) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "savePublicTabList: start");
                    WanRoomDataBase.get().publicDao().deletePublicTab();
                    WanRoomDataBase.get().publicDao().insertPublicTab(publicTabList);
                } catch (Exception e) {
                    SLog.d(TAG, "savePublicTabList fail");
                }
            }
        });
    }

    private void fetchTabFromDb() {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "fetchTabFromDb: start");
                    List<PublicTab> publicTabList = WanRoomDataBase.get().publicDao().queryPublicTab();
                    if (!CollectionUtil.isEmpty(publicTabList)) {
                        Collections.sort(publicTabList, mPublicTabComparator);
                        mPublicTabPairLD.postValue(new Pair<>(false, publicTabList));
                        mIsFirstLoad = false;
                    }
                } catch (Exception e) {
                    SLog.d(TAG, "fetchTabFromDb: fail");
                }
            }
        });
    }

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }

    public MutableLiveData<Pair<Boolean, List<PublicTab>>> getPublicTabLD() {
        return mPublicTabPairLD;
    }

    public List<PublicTab> getPublicTabList() {
        Pair<Boolean, List<PublicTab>> pair = mPublicTabPairLD.getValue();
        return pair != null ? pair.second : new ArrayList<PublicTab>();
    }
}
