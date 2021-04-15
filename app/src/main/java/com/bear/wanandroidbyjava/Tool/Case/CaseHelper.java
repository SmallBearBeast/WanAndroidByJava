package com.bear.wanandroidbyjava.Tool.Case;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.bear.wanandroidbyjava.Tool.Case.Info.LoadingCaseInfo;
import com.bear.wanandroidbyjava.Tool.Case.Info.NoDataCaseInfo;
import com.bear.wanandroidbyjava.Tool.Case.Info.NetErrorCaseInfo;

import java.util.HashMap;
import java.util.Map;

public class CaseHelper {
    public static final int CASE_TYPE_NONE = -1;
    public static final int CASE_TYPE_LOADING = 1;
    public static final int CASE_TYPE_NET_ERROR = 2;
    public static final int CASE_TYPE_NO_DATA = 3;
    public static final int CASE_TYPE_ROOM_EMPTY = CASE_TYPE_NO_DATA + 1;
    public static final int CASE_TYPE_TALK_EMPTY = CASE_TYPE_ROOM_EMPTY + 1;
    public static final int CASE_TYPE_REC_EMPTY = CASE_TYPE_TALK_EMPTY + 1;

    @IntDef({CASE_TYPE_LOADING, CASE_TYPE_NET_ERROR, CASE_TYPE_NO_DATA,
            CASE_TYPE_ROOM_EMPTY, CASE_TYPE_TALK_EMPTY, CASE_TYPE_REC_EMPTY, CASE_TYPE_NONE})
    @interface Type {

    }

    private static Map<Integer, CaseInfo> caseInfoMap = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        register(CASE_TYPE_LOADING, new LoadingCaseInfo());
        register(CASE_TYPE_NET_ERROR, new NetErrorCaseInfo());
        register(CASE_TYPE_NO_DATA, new NoDataCaseInfo());
    }

    private static void register(@CaseHelper.Type int type, @NonNull CaseInfo caseInfo) {
        if (caseInfoMap.containsKey(type)) {
            throw new RepeatedTypeException();
        }
        caseInfoMap.put(type, caseInfo);
    }

    static CaseInfo get(int type) {
        return caseInfoMap.get(type);
    }

    public static void showLoading(@NonNull CaseView caseView) {
        caseView.show(CASE_TYPE_LOADING);
    }

    public static void showNetError(@NonNull CaseView caseView) {
        caseView.show(CASE_TYPE_NET_ERROR);
    }

    public static void showNoData(@NonNull CaseView caseView) {
        caseView.show(CASE_TYPE_NO_DATA);
    }

    public static void hide(@NonNull CaseView caseView) {
        caseView.hide();
    }

    private static class RepeatedTypeException extends RuntimeException {
        public RepeatedTypeException() {
            super("Repeated type");
        }
    }
}
