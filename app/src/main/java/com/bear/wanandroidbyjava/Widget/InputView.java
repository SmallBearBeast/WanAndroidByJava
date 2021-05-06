package com.bear.wanandroidbyjava.Widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bear.wanandroidbyjava.R;

import java.util.ArrayList;
import java.util.List;


public class InputView extends ConstraintLayout implements View.OnFocusChangeListener, TextWatcher {
    private static final int INVALID_ID = -1;
    public static final int ACTION_NONE = 1;
    public static final int ACTION_CLEAR = 1 << 1;
    public static final int ACTION_PASSWORD = 1 << 2;
    public static final int ACTION_COPY = 1 << 3;
    public static final int ACTION_PASTE = 1 << 4;
    private static final int[] ACTION_TYPE_ARRAY = new int[]{
            ACTION_CLEAR, ACTION_PASSWORD, ACTION_COPY, ACTION_PASTE
    };
    private ImageView leftIconIv;
    private EditText inputEt;
    private LinearLayout actionLayout;
    private View underlineView;

    private int leftIconRes = INVALID_ID;
    private int leftIconSize = dp2px(25);
    private int inputMarginStart = dp2px(5);
    private String inputHint = "";
    private int inputTextSize = sp2px(14);
    private int inputTextColor = Color.BLACK;
    private int inputMarginEnd = dp2px(0);
    private int actionIconSize = dp2px(22);
    private int actionOffset = dp2px(0);
    private int actionType = ACTION_NONE;
    private int normalColor = Color.BLACK;
    private int focusColor = Color.BLACK;
    private final List<AbstractAction> actionList = new ArrayList<>();
    private EditListener editListener;

    public InputView(@NonNull Context context) {
        this(context, null);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAction();
        initAttr(attrs);
        initView();
        dispatchFocusChange(false);
    }

    private void initAction() {
        actionList.clear();
        actionList.add(new LeftIconAction());
        actionList.add(new UnderlineAction());
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputView);
        leftIconRes = typedArray.getResourceId(R.styleable.InputView_iv_left_icon_res, leftIconRes);
        leftIconSize = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_left_icon_size, leftIconSize);
        inputMarginStart = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_input_margin_start, inputMarginStart);
        inputMarginEnd = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_input_margin_end, inputMarginEnd);
        inputHint = typedArray.getString(R.styleable.InputView_iv_input_hint);
        inputTextSize = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_input_text_size, inputTextSize);
        inputTextColor = typedArray.getColor(R.styleable.InputView_iv_input_text_color, inputTextColor);
        actionIconSize = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_action_icon_size, actionIconSize);
        actionOffset = typedArray.getDimensionPixelSize(R.styleable.InputView_iv_action_offset, actionOffset);
        actionType = typedArray.getInt(R.styleable.InputView_iv_action_type, actionType);
        normalColor = typedArray.getColor(R.styleable.InputView_iv_normal_color, normalColor);
        focusColor = typedArray.getColor(R.styleable.InputView_iv_focus_color, focusColor);
        typedArray.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_input_view, this);
        initLeftIconIv();
        initInputEt();
        initActionLayout();
        initUnderlineView();
        setNormalColor(normalColor);
        setFocusColor(focusColor);
    }

    private void initLeftIconIv() {
        leftIconIv = findViewById(R.id.leftIconIv);
        leftIconIv.setImageTintMode(PorterDuff.Mode.SRC_IN);
        setLeftIconSize(leftIconSize);
        setLeftIconRes(leftIconRes);
    }

    private void initInputEt() {
        inputEt = findViewById(R.id.inputEt);
        setInputMarginStart(inputMarginStart);
        setInputMarginEnd(inputMarginEnd);
        setInputHint(inputHint);
        setInputTextSize(inputTextSize);
        setInputTextColor(inputTextColor);
        if ((actionType & ACTION_PASSWORD) == ACTION_PASSWORD) {
            inputEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        inputEt.setOnFocusChangeListener(this);
        inputEt.addTextChangedListener(this);
    }

    private void initActionLayout() {
        actionLayout = findViewById(R.id.actionLayout);
        setActionType(actionType);
    }

    private void initUnderlineView() {
        underlineView = findViewById(R.id.underlineView);
    }

    private void showSoftInput(View view) {
        try {
            Context context = view.getContext();
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.requestFocus();
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideSoftInput(View view) {
        try {
            Context context = view.getContext();
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestFocus(boolean focus) {
        if (inputEt.hasFocus() == focus) {
            return;
        }
        inputEt.setFocusable(focus);
        inputEt.setFocusableInTouchMode(focus);
        if (focus) {
            inputEt.requestFocus();
            showSoftInput(inputEt);
        } else {
            inputEt.clearFocus();
            hideSoftInput(inputEt);
        }
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    private int sp2px(float spValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

    private View createActionView(boolean firstActionView, boolean lastActionView, int actionType) {
        ImageView actionView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(actionIconSize, actionIconSize);
        lp.setMarginStart(firstActionView ? 0 : actionOffset);
        actionView.setLayoutParams(lp);
        actionView.setImageTintList(ColorStateList.valueOf(normalColor));
        actionView.setImageTintMode(PorterDuff.Mode.SRC_IN);
        actionView.setTag(actionType);
        int padding = dp2px(4);
        actionView.setPadding(firstActionView ? padding : padding / 2, padding, lastActionView ? padding : padding / 2, padding);
        final AbstractAction action = createAction(actionType);
        actionList.add(action);
        actionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                action.onAction();
            }
        });
        if (actionType == ACTION_CLEAR) {
            actionView.setImageResource(R.drawable.ic_input_view_clear);
        } else if (actionType == ACTION_PASSWORD) {
            actionView.setImageResource(R.drawable.ic_input_view_password_visible);
        } else if (actionType == ACTION_COPY) {
            actionView.setImageResource(R.drawable.ic_input_view_copy);
        } else if (actionType == ACTION_PASTE) {
            actionView.setImageResource(R.drawable.ic_input_view_paste);
        } else {
            actionView = null;
        }
        return actionView;
    }

    public void setLeftIconRes(int leftIconRes) {
        if (leftIconRes != INVALID_ID) {
            this.leftIconRes = leftIconRes;
            leftIconIv.setImageResource(leftIconRes);
        }
    }

    public void setLeftIconSize(int leftIconSize) {
        if (leftIconSize >= 0 || leftIconSize == ViewGroup.LayoutParams.MATCH_PARENT
                || leftIconSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            this.leftIconSize = leftIconSize;
            LayoutParams lp = (LayoutParams) leftIconIv.getLayoutParams();
            lp.width = leftIconSize;
            lp.height = leftIconSize;
            leftIconIv.setLayoutParams(lp);
        }
    }

    public void setInputMarginStart(int inputMarginStart) {
        if (inputMarginStart >= 0) {
            this.inputMarginStart = inputMarginStart;
            LayoutParams lp = (LayoutParams) inputEt.getLayoutParams();
            lp.setMarginStart(inputMarginStart);
            inputEt.setLayoutParams(lp);
        }
    }

    public void setInputMarginEnd(int inputMarginEnd) {
        if (inputMarginEnd >= 0) {
            this.inputMarginEnd = inputMarginEnd;
            LayoutParams lp = (LayoutParams) inputEt.getLayoutParams();
            lp.setMarginEnd(inputMarginEnd);
            inputEt.setLayoutParams(lp);
        }
    }

    public void setInputHint(String inputHint) {
        if (inputHint != null) {
            this.inputHint = inputHint;
            inputEt.setHint(inputHint);
        }
    }

    public void setInputTextSize(int inputTextSize) {
        if (inputTextSize >= 0) {
            this.inputTextSize = inputTextSize;
            inputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
        }
    }

    public void setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        inputEt.setTextColor(inputTextColor);
    }

    public void setActionIconSize(int actionIconSize) {
        if (actionIconSize >= 0 || actionIconSize == ViewGroup.LayoutParams.MATCH_PARENT
                || actionIconSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            this.actionIconSize = actionIconSize;
            for (int i = 0; i < actionLayout.getChildCount(); i++) {
                View view = actionLayout.getChildAt(i);
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.width = actionIconSize;
                lp.height = actionIconSize;
                view.setLayoutParams(lp);
            }
        }
    }

    public void setActionOffset(int actionOffset) {
        if (actionOffset >= 0) {
            this.actionOffset = actionOffset;
            for (int i = 0; i < actionLayout.getChildCount(); i++) {
                View view = actionLayout.getChildAt(i);
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                lp.setMarginStart(i == 0 ? 0 : actionOffset);
                view.setLayoutParams(lp);
            }
        }
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
        initAction();
        actionLayout.removeAllViews();
        if ((actionType & ACTION_NONE) == ACTION_NONE) {
            actionLayout.setVisibility(View.GONE);
            return;
        }
        List<Integer> actionTypeList = new ArrayList<>();
        for (int type : ACTION_TYPE_ARRAY) {
            if ((actionType & type) == type) {
                actionTypeList.add(type);
            }
        }
        if (!actionTypeList.isEmpty()) {
            actionLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < actionTypeList.size(); i++) {
                View actionView = createActionView(i == 0, i == actionTypeList.size() - 1, actionTypeList.get(i));
                if (actionView != null) {
                    actionLayout.addView(actionView);
                }
            }
        }
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        boolean focus = inputEt.hasFocus();
        if (!focus) {
            leftIconIv.setImageTintList(ColorStateList.valueOf(normalColor));
            underlineView.setBackgroundColor(normalColor);
            for (int i = 0; i < actionLayout.getChildCount(); i++) {
                ImageView iv = (ImageView) actionLayout.getChildAt(i);
                iv.setImageTintList(ColorStateList.valueOf(normalColor));
            }
        }
    }

    public void setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        boolean focus = inputEt.hasFocus();
        if (focus) {
            leftIconIv.setImageTintList(ColorStateList.valueOf(focusColor));
            underlineView.setBackgroundColor(focusColor);
            for (int i = 0; i < actionLayout.getChildCount(); i++) {
                ImageView iv = (ImageView) actionLayout.getChildAt(i);
                if (iv.getTag() instanceof Integer) {
                    int type = (int) iv.getTag();
                    if (type == ACTION_PASSWORD) {
                        iv.setImageTintList(ColorStateList.valueOf(focusColor));
                    }
                }
            }
        }
    }

    public void setEditListener(EditListener listener) {
        editListener = listener;
    }

    public String getInputText() {
        return String.valueOf(inputEt.getText());
    }

    public EditText getInputEt() {
        return inputEt;
    }

    public ImageView getLeftIconIv() {
        return leftIconIv;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = String.valueOf(s);
        dispatchTextChange(text);
        if (editListener != null) {
            editListener.onEdit(this, text);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        requestFocus(hasFocus);
        dispatchFocusChange(hasFocus);
    }

    private void dispatchFocusChange(boolean focus) {
        for (AbstractAction action : actionList) {
            action.onFocusChange(focus);
        }
    }

    private void dispatchTextChange(String text) {
        for (AbstractAction action : actionList) {
            action.onTextChange(text);
        }
    }

    public interface EditListener {
        void onEdit(InputView inputView, String text);
    }

    private abstract static class AbstractAction {
        void onFocusChange(boolean focus) {

        }

        void onTextChange(String text) {

        }

        void onAction() {

        }
    }

    private static class NoneAction extends AbstractAction {

    }

    private class LeftIconAction extends AbstractAction {
        @Override
        public void onFocusChange(boolean focus) {
            int color = focus ? focusColor : normalColor;
            leftIconIv.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    private class UnderlineAction extends AbstractAction {
        @Override
        public void onFocusChange(boolean focus) {
            int color = focus ? focusColor : normalColor;
            underlineView.setBackgroundColor(color);
            ViewGroup.LayoutParams lp = underlineView.getLayoutParams();
            lp.height = dp2px(focus ? 2 : 1);
            underlineView.setLayoutParams(lp);
        }
    }

    private class ClearAction extends AbstractAction {
        @Override
        void onFocusChange(boolean focus) {
            View view = actionLayout.findViewWithTag(ACTION_CLEAR);
            String text = String.valueOf(inputEt.getText());
            if (focus && !text.isEmpty()) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onTextChange(String text) {
            View view = actionLayout.findViewWithTag(ACTION_CLEAR);
            if (text != null && !text.isEmpty()) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onAction() {
            inputEt.setText("");
        }
    }

    private class PasswordAction extends AbstractAction {
        @Override
        public void onFocusChange(boolean focus) {
            View view = actionLayout.findViewWithTag(ACTION_PASSWORD);
            if (focus) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onAction() {
            ImageView imageView = actionLayout.findViewWithTag(ACTION_PASSWORD);
            int selectionStart = inputEt.getSelectionStart();
            int selectionEnd = inputEt.getSelectionEnd();
            if (inputEt.getTransformationMethod() instanceof PasswordTransformationMethod) {
                inputEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageView.setImageTintList(ColorStateList.valueOf(focusColor));
            } else {
                inputEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageView.setImageTintList(ColorStateList.valueOf(normalColor));
            }
            inputEt.setSelection(selectionStart, selectionEnd);
        }
    }

    private class CopyAction extends AbstractAction {
        @Override
        void onFocusChange(boolean focus) {
            View view = actionLayout.findViewWithTag(ACTION_COPY);
            String text = String.valueOf(inputEt.getText());
            if (focus && !text.isEmpty()) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onTextChange(String text) {
            View view = actionLayout.findViewWithTag(ACTION_COPY);
            if (text != null && !text.isEmpty()) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onAction() {
            String text = String.valueOf(inputEt.getText());
            if (copy(text)) {
                Toast.makeText(getContext(), "文本已复制", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PasteAction extends AbstractAction {
        @Override
        void onFocusChange(boolean focus) {
            View view = actionLayout.findViewWithTag(ACTION_PASTE);
            if (focus) {
                setViewVisible(view);
            } else {
                setViewGone(view);
            }
        }

        @Override
        void onAction() {
            Editable editable = inputEt.getEditableText();
            String pasteText = paste();
            editable.insert(inputEt.getSelectionStart(), pasteText);
        }
    }

    private void setViewVisible(@Nullable View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setViewGone(@Nullable View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private boolean copy(String text) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("label", text);
        if (manager != null) {
            try {
                manager.setPrimaryClip(data);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String paste() {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = manager.getPrimaryClip();
        String text = "";
        if (clipData != null) {
            ClipData.Item item = clipData.getItemAt(0);
            if (item != null) {
                text = String.valueOf(item.getText());
            }
        }
        return text;
    }

    private AbstractAction createAction(int actionType) {
        if (actionType == ACTION_CLEAR) {
            return new ClearAction();
        } else if (actionType == ACTION_PASSWORD) {
            return new PasswordAction();
        } else if (actionType == ACTION_COPY) {
            return new CopyAction();
        } else if (actionType == ACTION_PASTE) {
            return new PasteAction();
        } else {
            return new NoneAction();
        }
    }
}
