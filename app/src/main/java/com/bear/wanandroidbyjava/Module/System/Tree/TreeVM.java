package com.bear.wanandroidbyjava.Module.System.Tree;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bear.wanandroidbyjava.Data.Bean.Tree;
import com.bear.wanandroidbyjava.Controller.TreeController;
import com.bear.wanandroidbyjava.Storage.KV.SpValHelper;
import com.example.libbase.Util.CollectionUtil;
import com.example.libbase.Util.NetWorkUtil;
import com.example.liblog.SLog;

import java.util.List;

public class TreeVM extends ViewModel implements TreeController.TreeDataListener{
    private static final String TAG = "TreeVM";
    public static final byte LOAD_NET_ERROR = 1;
    public static final byte LOAD_NO_DATA = 2;
    public static final byte LOAD_PROGRESS_SHOW = 3;
    public static final byte LOAD_PROGRESS_HIDE = 4;
    private boolean isFirstLoadComplete = false;
    private boolean isLoadDone = false;
    private TreeController treeController = new TreeController();
    private MutableLiveData<Byte> loadStateLD = new MutableLiveData<>();
    private MutableLiveData<List<Tree>> treeLD = new MutableLiveData<>();

    public void loadTreeData(boolean includeStorage) {
        if (includeStorage) {
            treeController.loadDataFromStorage(this);
        }
        if (!NetWorkUtil.isConnected()) {
            SLog.d(TAG, "loadTreeData: net is not connected");
            loadStateLD.setValue(LOAD_NET_ERROR);
            return;
        }
        if (!SpValHelper.hasTreeStorageData.get()) {
            loadStateLD.setValue(LOAD_PROGRESS_SHOW);
        }
        treeController.loadDataFromNet(this);
    }

    public MutableLiveData<List<Tree>> getTreeLD() {
        return treeLD;
    }

    public MutableLiveData<Byte> getLoadStateLD() {
        return loadStateLD;
    }

    public boolean isFirstLoadComplete() {
        return isFirstLoadComplete;
    }

    @Override
    public void onLoad(List<Tree> treeList, boolean fromNet) {
        if (fromNet) {
            if (!isLoadDone) {
                isLoadDone = true;
            }
            isFirstLoadComplete = true;
            loadStateLD.setValue(LOAD_PROGRESS_HIDE);
            if (!CollectionUtil.isEmpty(treeList)) {
                SpValHelper.hasTreeStorageData.set(true);
                treeLD.setValue(treeList);
            } else {
                List<Tree> lastTreeList = treeLD.getValue();
                if (CollectionUtil.isEmpty(lastTreeList)) {
                    loadStateLD.setValue(LOAD_NO_DATA);
                }
            }
        } else {
            if (!isLoadDone && !CollectionUtil.isEmpty(treeList)) {
                treeLD.setValue(treeList);
            }
        }
    }
}
