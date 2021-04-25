package com.bear.wanandroidbyjava.Manager;

import android.util.Log;

import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Data.NetBean.TreeBean;
import com.bear.wanandroidbyjava.Net.NetUrl;
import com.bear.wanandroidbyjava.Net.WanOkCallback;
import com.bear.wanandroidbyjava.Net.WanResponce;
import com.bear.wanandroidbyjava.Net.WanTypeToken;
import com.bear.wanandroidbyjava.Storage.SysStorage;
import com.bear.wanandroidbyjava.Tool.Helper.DataHelper;
import com.example.libbase.Executor.BgThreadExecutor;
import com.example.libbase.Executor.MainThreadExecutor;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.StringUtil;
import com.example.liblog.SLog;
import com.example.libokhttp.OkHelper;

import java.util.ArrayList;
import java.util.List;

public class TreeManager {
    private static final String TAG = "TreeManager";
    public void loadDataFromNet(final TreeDataListener listener) {
        SLog.d(TAG, "loadTreeDataFromNet: start");
        OkHelper.getInstance().getMethod(NetUrl.TREE, new WanOkCallback<List<TreeBean>>(WanTypeToken.TREE_TOKEN) {
            @Override
            protected void onSuccess(WanResponce<List<TreeBean>> data) {
                Log.d(TAG, "onSuccess: data = " + data);
                if (data != null) {
                    SLog.d(TAG, "loadTreeDataFromNet: data.errorCode = " + data.errorCode + (StringUtil.isEmpty(data.errorMsg) ? "" : ", data.errorMsg = " + data.errorMsg));
                    List<Tree> treeList = DataHelper.treeBeanToTree(data.data);
                    SLog.d(TAG, "loadTreeDataFromNet: treeList.size = " + treeList.size() + ", treeList = " + treeList);
                    if (!CollectionUtil.isEmpty(treeList)) {
                        SysStorage.saveTreeList(treeList);
                    }
                    callTreeDataListener(listener, treeList, true);
                }
            }

            @Override
            protected void onFail() {
                SLog.d(TAG, "loadTreeDataFromNet: onFail");
                callTreeDataListener(listener, new ArrayList<Tree>(), true);
            }
        });
    }

    public void loadDataFromStorage(final TreeDataListener listener) {
        BgThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Tree> treeList = SysStorage.getTreeList();
                SLog.d(TAG, "loadDataFromStorage: treeList = " + treeList);
                callTreeDataListener(listener, treeList, false);
            }
        });
    }

    private void callTreeDataListener(final TreeDataListener listener, final List<Tree> treeList, final boolean fromNet) {
        if (listener == null) {
            return;
        }
        MainThreadExecutor.post(new Runnable() {
            @Override
            public void run() {
                listener.onLoad(treeList, fromNet);
            }
        });
    }

    public interface TreeDataListener {
        void onLoad(List<Tree> treeList, boolean fromNet);
    }
}
