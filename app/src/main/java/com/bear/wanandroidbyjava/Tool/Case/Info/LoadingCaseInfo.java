package com.bear.wanandroidbyjava.Tool.Case.Info;

import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseInfo;

public class LoadingCaseInfo extends CaseInfo {
    @Override
    protected int type() {
        return CaseHelper.CASE_TYPE_LOADING;
    }

    @Override
    protected boolean showProgress() {
        return true;
    }

    @Override
    protected int title() {
        return R.string.str_data_loading;
    }
}
