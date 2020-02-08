package com.bear.wanandroidbyjava.Module.System.Tree;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Bean.Tree;
import com.bear.wanandroidbyjava.NetBean.TreeBean;
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

public class TreeVM extends ViewModel {
    private static final String TAG = "TreeVM";

    public MutableLiveData<List<Tree>> mTreeLD = new MutableLiveData<>();

    public void fetchTree() {
        OkHelper.getInstance().getMethod(NetUrl.TREE, new OkCallback<WanResponce<List<TreeBean>>>(new TypeToken<WanResponce<List<TreeBean>>>(){}) {
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
                        SLog.d(TAG, "fetchTree: treeBeanList.size = " + treeBeanList.size());
                        SLog.d(TAG, "fetchTree: treeBeanList = " + treeBeanList);
                    }
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "fetchTree: onFail");
            }
        });
    }
}
