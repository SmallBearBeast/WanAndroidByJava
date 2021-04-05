package com.bear.wanandroidbyjava.Module.System.Tree;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.Tree;
import com.bear.wanandroidbyjava.NetBean.TreeBean;
import com.bear.wanandroidbyjava.NetBean.WanOkCallback;
import com.bear.wanandroidbyjava.NetBean.WanResponce;
import com.bear.wanandroidbyjava.NetBean.WanTypeToken;
import com.bear.wanandroidbyjava.NetUrl;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;

public class TreeVM extends ViewModel {
    private static final String TAG = "TreeVM";
    private boolean mIsFirstLoad = true;
    private MutableLiveData<Boolean> mShowProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<Tree>> mTreeLD = new MutableLiveData<>();

    public void fetchTree() {
        SLog.d(TAG, "fetchTree: start");
        mShowProgressLD.postValue(true);
        OkHelper.getInstance().getMethod(NetUrl.TREE, new WanOkCallback<List<TreeBean>>(WanTypeToken.TREE_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<TreeBean>> data) {
                if (data != null) {
                    SLog.d(TAG, "fetchTree: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    if (CollectionUtil.isEmpty(data.data)) {
                        SLog.d(TAG, "fetchTree: treeBeanList is empty");
                    } else {
                        List<TreeBean> treeBeanList = data.data;
                        List<Tree> treeList = new ArrayList<>();
                        for (TreeBean treeBean : treeBeanList) {
                            treeList.add(treeBean.toTree());
                        }
                        mTreeLD.postValue(treeList);
                        mIsFirstLoad = false;
                        SLog.d(TAG, "fetchTree: treeBeanList.size = " + treeBeanList.size() + ", treeBeanList = " + treeBeanList);
                    }
                }
                mShowProgressLD.postValue(false);
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTree: onFail");
                mShowProgressLD.postValue(false);
            }
        });
    }

    public MutableLiveData<List<Tree>> getTreeLD() {
        return mTreeLD;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return mShowProgressLD;
    }

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }
}
