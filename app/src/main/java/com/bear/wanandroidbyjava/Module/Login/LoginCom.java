package com.bear.wanandroidbyjava.Module.Login;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bear.libcomponent.ComponentService;
import com.bear.libcomponent.ViewComponent;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Widget.InputView;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;

public class LoginCom extends ViewComponent<LoginFrag> implements View.OnClickListener {
    private InputView userInputView;
    private InputView passwordInputView;

    private LoginRegisterVM loginRegisterVM;

    @Override
    protected void onCreate() {
        loginRegisterVM = new ViewModelProvider(getDependence()).get(LoginRegisterVM.class);
        loginRegisterVM.getLoginSuccessLD().observe(getDependence(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    getComActivity().finish();
                }
            }
        });
    }

    @Override
    protected void onCreateView() {
        clickListener(this, R.id.loginBt, R.id.goToRegisterView);
        userInputView = findViewById(R.id.userInputView);
        passwordInputView = findViewById(R.id.passwordInputView);
        passwordInputView.getInputEt().setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordInputView.getInputEt().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    login();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.loginBt) {
            login();
        } else if (viewId == R.id.goToRegisterView) {
            ComponentService.get().getComponent(ILoginRegisterCom.class).goToRegisterPage();
        }
    }

    private void login() {
        String userName = userInputView.getInputText().trim();
        String password = passwordInputView.getInputText().trim();
        if (checkInput(userName, password)) {
            loginRegisterVM.login(userName, password);
        }
    }

    private boolean checkInput(String userName, String password) {
        if (StringUtil.isEmpty(userName)) {
            ToastUtil.showToast(R.string.str_user_name_not_empty);
            return false;
        }
        if (StringUtil.isEmpty(password)) {
            ToastUtil.showToast(R.string.str_password_not_empty);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroyView() {
        clear(userInputView, passwordInputView);
    }
}
