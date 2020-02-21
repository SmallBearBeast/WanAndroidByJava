package com.bear.wanandroidbyjava.Module.Public;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.PublicTab;
import com.bear.wanandroidbyjava.NetBean.PublicTabBean;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PublicTabVM extends ViewModel {
    private static final String TAG = "PublicTabVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<PublicTab>> mPublicTabLD = new MutableLiveData<>();

    public void fetchTab() {
        SLog.d(TAG, "fetchTab: start");
        mShowProgressLD.postValue(true);
        OkHelper.getInstance().getMethod(NetUrl.PUBLIC_TAB, new OkCallback<WanResponce<List<PublicTabBean>>>(new TypeToken<WanResponce<List<PublicTabBean>>>(){}) {
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
                        mPublicTabLD.postValue(publicTabList);
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

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }

    public MutableLiveData<List<PublicTab>> getPublicTabLD() {
        return mPublicTabLD;
    }
}
