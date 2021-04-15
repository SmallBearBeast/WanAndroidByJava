package com.bear.wanandroidbyjava.Tool.Case;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bear.wanandroidbyjava.R;

public class CaseView extends FrameLayout {
    private ImageView caseCoverIv;
    private TextView caseTitleTv;
    private TextView caseDescriptionTv;
    private ProgressBar caseProgressBar;
    private Button caseClickBt;
    private CaseInfo caseInfo;

    public CaseView(@NonNull Context context) {
        this(context, null);
    }

    public CaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(@NonNull CaseInfo caseInfo) {
        int layoutId =
                caseInfo.layoutId() == CaseInfo.NONE_ID ? R.layout.view_case : caseInfo.layoutId();
        View contentView = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        caseCoverIv = contentView.findViewById(R.id.case_cover_iv);
        caseTitleTv = contentView.findViewById(R.id.case_title_tv);
        caseDescriptionTv = contentView.findViewById(R.id.case_description_tv);
        caseProgressBar = contentView.findViewById(R.id.case_progress_bar);
        caseClickBt = contentView.findViewById(R.id.case_click_bt);
        addView(
                contentView,
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void show(@CaseHelper.Type int type, ViewInitListener listener) {
        caseInfo = CaseHelper.get(type);
        if (caseInfo == null) {
            return;
        }
        hide();
        setVisibility(VISIBLE);
        initView(caseInfo);
        if (listener != null) {
            listener.onInit(this);
        }
        setUpView(caseInfo);
    }

    public void show(@CaseHelper.Type int type) {
        show(type, null);
    }

    private void setUpView(@NonNull CaseInfo caseInfo) {
        if (caseProgressBar != null) {
            caseProgressBar.setVisibility(View.GONE);
            if (caseInfo.showProgress()) {
                caseProgressBar.setVisibility(View.VISIBLE);
            }
        }
        if (caseCoverIv != null) {
            caseCoverIv.setVisibility(View.GONE);
            if (caseInfo.cover() != -1) {
                caseCoverIv.setVisibility(View.VISIBLE);
                caseCoverIv.setImageResource(caseInfo.cover());
            }
        }
        if (caseTitleTv != null) {
            caseTitleTv.setVisibility(View.GONE);
            if (caseInfo.title() != -1) {
                caseTitleTv.setVisibility(View.VISIBLE);
                caseTitleTv.setText(caseInfo.title());
            }
        }
        if (caseDescriptionTv != null) {
            caseDescriptionTv.setVisibility(View.GONE);
            if (caseInfo.description() != -1) {
                caseDescriptionTv.setVisibility(View.VISIBLE);
                caseDescriptionTv.setText(caseInfo.description());
            }
        }
        if (caseClickBt != null) {
            caseClickBt.setVisibility(View.GONE);
            if (caseInfo.clickText() != -1) {
                caseClickBt.setVisibility(View.VISIBLE);
                caseClickBt.setText(caseInfo.clickText());
            }
        }
    }

    public void hide() {
        setVisibility(GONE);
        removeAllViews();
    }

    public @CaseHelper.Type int getCaseType() {
        return caseInfo == null ? CaseHelper.CASE_TYPE_NONE : caseInfo.type();
    }

    public ImageView getCaseCoverIv() {
        return caseCoverIv;
    }

    public TextView getCaseTitleTv() {
        return caseTitleTv;
    }

    public TextView getCaseDescriptionTv() {
        return caseDescriptionTv;
    }

    public ProgressBar getCaseProgressBar() {
        return caseProgressBar;
    }

    public Button getCaseClickBt() {
        return caseClickBt;
    }

    public interface ViewInitListener {
        void onInit(@NonNull CaseView caseView);
    }
}
