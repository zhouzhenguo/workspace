package com.kesetel.mobile.contract.user;

import com.kesetel.mobile.entity.setting.User;
import com.kesetel.mobile.present.BasePresent;
import com.kesetel.mobile.ui.BaseView;

/**
 * 契约类（定义View层与P层交互借口）
 * Created by Administrator on 2017/4/24.
 */

public interface LoginContract {
    public interface ILoginView extends BaseView {
        public void onLoginSuccess(User user);
        public void onUserNotExit();
        public void onLoginFail(String code);
    }

    public interface ILoginPresent extends BasePresent{
        public void login(String username, String password);
    }
}
