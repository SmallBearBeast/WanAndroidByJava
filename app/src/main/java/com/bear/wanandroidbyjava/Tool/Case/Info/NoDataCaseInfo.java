package com.bear.wanandroidbyjava.Tool.Case.Info;

import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseInfo;

public class NoDataCaseInfo extends CaseInfo {
    @Override
    protected int type() {
        return CaseHelper.CASE_TYPE_NO_DATA;
    }

    @Override
    protected int cover() {
        return R.drawable.ic_empty;
    }

    @Override
    protected int title() {
        return R.string.str_no_data;
    }

    @Override
    protected int description() {
        return R.string.str_refresh_again;
    }
}