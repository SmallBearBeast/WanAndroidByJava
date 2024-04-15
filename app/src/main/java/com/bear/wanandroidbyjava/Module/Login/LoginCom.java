package com.bear.wanandroidbyjava.Module.Login;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.bear.libcomponent.component.FragmentComponent;
import com.bear.wanandroidbyjava.Data.Bean.UserInfoBean;
import com.bear.wanandroidbyjava.R;
import com.bear.wanandroidbyjava.Tool.Util.OtherUtil;
import com.bear.wanandroidbyjava.WanConstant;
import com.bear.wanandroidbyjava.Widget.InputView;
import com.example.libbase.Util.NetWorkUtil;
import com.example.libbase.Util.ResourceUtil;
import com.example.libbase.Util.StringUtil;
import com.example.libbase.Util.ToastUtil;

public class LoginCom extends FragmentComponent implements View.OnClickListener {

    private InputView userInputView;
    private InputView passwordInputView;

    private LoginRegisterVM loginRegisterVM;

    public LoginCom(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void onCreate() {
        loginRegisterVM = new ViewModelProvider(getFragment()).get(LoginRegisterVM.class);
        loginRegisterVM.getLoginStateLD().observe(getFragment(), pair -> {
            Boolean success = pair.first;
            UserInfoBean userInfoBean = pair.second;
            if (success != null && success) {
                ToastUtil.showToast(ResourceUtil.getString(R.string.str_login_success));
                loginRegisterVM.fetchUserData();
            } else {
                ToastUtil.showToast(R.string.str_login_fail);
            }
        });
        loginRegisterVM.getFetchUserDataResultLD().observe(getFragment(), userDataBean -> {
            Intent intent = new Intent();
            intent.putExtra(WanConstant.BUNDLE_KEY_USER_DATA, userDataBean);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });
    }

    @Override
    protected void onCreateView() {
        setOnClickListener(this, R.id.loginBt, R.id.goToRegisterView);
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
            getComponent(LoginRegisterCom.class).goToRegisterPage();
        }
    }

    private void login() {
        String userName = userInputView.getInputText().trim();
        String password = passwordInputView.getInputText().trim();
        if (!NetWorkUtil.isConnected()) {
            ToastUtil.showToast(R.string.str_net_error_to_try_again);
            return;
        }
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
        OtherUtil.clear(userInputView, passwordInputView);
    }
}
