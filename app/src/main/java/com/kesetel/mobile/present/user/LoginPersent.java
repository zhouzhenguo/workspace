package com.kesetel.mobile.present.user;

import com.kesetel.mobile.contract.user.LoginContract;
import com.kesetel.mobile.entity.setting.User;
import com.kesetel.mobile.model.biz.user.LoginBiz;

/**
 * P层(View层与逻辑层的通信桥梁) -- 登录功能
 * Created by Administrator on 2017/4/24.
 */

public class LoginPersent implements LoginContract.ILoginPresent, LoginBiz.LoginCallback {
    private LoginContract.ILoginView loginView;
    private LoginBiz loginBiz;

    public LoginPersent(LoginContract.ILoginView loginView){
        this.loginView = loginView;
        loginBiz = new LoginBiz(this);
    }

    public void login(final String username, String password){
        loginBiz.login(username, password);
    }

    @Override
    public void onLoginSuccess(User user) {
        loginView.onLoginSuccess(user);
    }

    @Override
    public void onUserNotExit() {
        loginView.onUserNotExit();
    }

    @Override
    public void onLoginFail(String code) {
        loginView.onLoginFail(code);
    }

    @Override
    public void onResult(User user) {

    }
}
