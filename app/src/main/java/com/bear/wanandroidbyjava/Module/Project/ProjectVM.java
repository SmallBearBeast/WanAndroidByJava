package com.bear.wanandroidbyjava.Module.Project;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.ProjectTab;
import com.bear.wanandroidbyjava.NetBean.ProjectTabBean;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetUrl;
import com.bear.wanandroidbyjava.WanRoomDataBase;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ThreadUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkCallback;
import com.example.libokhttp.OkHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectVM extends ViewModel {
    private static final String TAG = "PublicTabVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<Pair<Boolean, List<ProjectTab>>> mProjectTabPairLD = new MutableLiveData<>();
    private Comparator<ProjectTab> mProjectTabComparator = new Comparator<ProjectTab>() {
        @Override
        public int compare(ProjectTab projectTab_1, ProjectTab projectTab_2) {
            if (projectTab_1.order < projectTab_2.order) {
                return -1;
            } else if (projectTab_1.order > projectTab_2.order){
                return 1;
            } else {
                return 0;
            }
        }
    };

    private void fetchProjectTabFromDb() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "fetchProjectTabFromDb: start");
                    List<ProjectTab> projectTabList = WanRoomDataBase.get().projectDao().queryProjectTab();
                    if (!CollectionUtil.isEmpty(projectTabList)) {
                        Collections.sort(projectTabList, mProjectTabComparator);
                        mProjectTabPairLD.postValue(new Pair<>(false, projectTabList));
                        mIsFirstLoad = false;
                    }
                } catch (Exception e) {
                    SLog.d(TAG, "fetchProjectTabFromDb: fail");
                }
            }
        });
    }

    public void fetchProjectTab() {
        SLog.d(TAG, "fetchProjectTab: start");
        mShowProgressLD.postValue(true);
        fetchProjectTabFromDb();
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
                        Collections.sort(projectTabList, mProjectTabComparator);
                        mProjectTabPairLD.postValue(new Pair<>(true, projectTabList));
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

    public void saveProjectTabList(final List<ProjectTab> projectTabList) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SLog.d(TAG, "saveProjectTabList: start");
                    WanRoomDataBase.get().projectDao().deleteProjectTab();
                    WanRoomDataBase.get().projectDao().insertProjectTab(projectTabList);
                } catch (Exception e) {
                    SLog.d(TAG, "saveProjectTabList fail");
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

    public MutableLiveData<Pair<Boolean, List<ProjectTab>>> getProjectTabPairLD() {
        return mProjectTabPairLD;
    }

    public List<ProjectTab> getProjectTabList() {
        Pair<Boolean, List<ProjectTab>> pair = mProjectTabPairLD.getValue();
        return pair != null ? pair.second : new ArrayList<ProjectTab>();
    }
}
