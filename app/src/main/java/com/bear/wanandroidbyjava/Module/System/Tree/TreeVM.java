package com.bear.wanandroidbyjava.Module.System.Tree;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Manager.TreeManager;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;

import java.util.List;

public class TreeVM extends ViewModel implements TreeManager.TreeDataListener{
    private static final String TAG = "TreeVM";
    private boolean isFirstLoad = true;
    private TreeManager treeManager= new TreeManager();
    private MutableLiveData<Boolean> showProgressLD = new MutableLiveData<>();
    private MutableLiveData<List<Tree>> treeLD = new MutableLiveData<>();

    public void loadTreeData() {
        treeManager.loadDataFromStorage(this);
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadTreeData: net is not connected");
            showProgressLD.postValue(false);
            return;
        }
        if (isFirstLoad) {
            showProgressLD.postValue(true);
        }
        treeManager.loadDataFromNet(this);
    }

    public MutableLiveData<List<Tree>> getTreeLD() {
        return treeLD;
    }

    public MutableLiveData<Boolean> getShowProgressLD() {
        return showProgressLD;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    @Override
    public void onLoad(List<Tree> treeList, boolean fromNet) {
        if (fromNet) {
            if (!CollectionUtil.isEmpty(treeList)) {
                treeLD.postValue(treeList);
                isFirstLoad = false;
            } else {
                List<Tree> lastTreeList = treeLD.getValue();
                if (CollectionUtil.isEmpty(lastTreeList)) {
                    treeLD.postValue(null);
                }
            }
            showProgressLD.postValue(false);
        } else {
            if (!CollectionUtil.isEmpty(treeList)) {
                treeLD.postValue(treeList);
            }
        }
    }
}
