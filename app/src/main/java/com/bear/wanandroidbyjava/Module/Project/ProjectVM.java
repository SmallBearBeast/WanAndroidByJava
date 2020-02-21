package com.bear.wanandroidbyjava.Module.Project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.ProjectTab;
import com.bear.wanandroidbyjava.NetBean.ProjectTabBean;
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

public class ProjectVM extends ViewModel {
    private static final String TAG = "PublicTabVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<ProjectTab>> mProjectTabLD = new MutableLiveData<>();

    public void fetchProjectTab() {
        SLog.d(TAG, "fetchProjectTab: start");
        mShowProgressLD.postValue(true);
        OkHelper.getInstance().getMethod(NetUrl.PROJECT_TAB, new OkCallback<WanResponce<List<ProjectTabBean>>>(new TypeToken<WanResponce<List<ProjectTabBean>>>(){}) {
            @Override
            protected void onSuccess(WanResponce<List<ProjectTabBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchProjectTab: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "fetchProjectTab: publicTabList is empty");
                    } else {
                        List<ProjectTab> projectTabList = new ArrayList<>();
                        List<ProjectTabBean> projectTabBeanList = data.data;
                        for (ProjectTabBean projectTabBean : projectTabBeanList) {
                            projectTabList.add(projectTabBean.toProjectTab());
                        }
                        SLog.d(TAG, "fetchProjectTab: onSuccess, projectTabList.size = " + projectTabList.size() + ", projectTabList = " + projectTabList);
                        mProjectTabLD.postValue(projectTabList);
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

    public MutableLiveData<List<ProjectTab>> getProjectTabLD() {
        return mProjectTabLD;
    }
}
