package com.bear.wanandroidbyjava.Tool.Case.Info;

import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Case.CaseHelper;
import com.bear.wanandroidbyjava.Tool.Case.CaseInfo;

public class NetErrorCaseInfo extends CaseInfo {
    @Override
    protected int type() {
        return CaseHelper.CASE_TYPE_NET_ERROR;
    }

    @Override
    protected int cover() {
        return R.drawable.ic_net_error;
    }

    @Override
    protected int title() {
        return R.string.str_net_error;
    }

    @Override
    protected int description() {
        return R.string.str_check_net_refresh_again;
    }
}
