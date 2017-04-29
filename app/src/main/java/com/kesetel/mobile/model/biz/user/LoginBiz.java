package com.kesetel.mobile.model.biz.user;

import android.text.TextUtils;

import com.kesetel.mobile.MyApplictaion;
import com.kesetel.mobile.model.biz.BizCallback;
import com.kesetel.mobile.model.dal.UserDal;
import com.kesetel.mobile.model.dal.db.dao.user.UserDao;
import com.kesetel.mobile.entity.setting.User;
import com.kesetel.mobile.helper.LogHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 业务逻辑层 -- 处理登录逻辑
 * Created by Administrator on 2017/4/24.
 */

public class LoginBiz {
    private UserDal loginDal;
    private LoginCallback loginCallback;

    public LoginBiz(LoginCallback loginCallback){
        loginDal = new UserDal();
        this.loginCallback = loginCallback;
    }

    public void login(String username, String password){
        // 逻辑处理， 首先判断用户名、密码不能为空
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            return;
        }

        // 调用DAL层实现数据交互
        loginDal.login(username, password, new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                User user = null;
                /**
                 * 通过Gson将后台相应数据解析为User对象
                 * user = gson.fromJson(response, User.class);
                 */

                user = new User();
                user.username = ""+System.currentTimeMillis();

                // 调用DAL层，保存数据库
                UserDao userDao = new UserDao(MyApplictaion.getAppContext());
                userDao.insert(user);

                //记录日志
                LogHelper.logFileAndLogcat("LoginBiz", "Login success");

                if(loginCallback != null){
                    loginCallback.onLoginSuccess(user);
                    loginCallback.onUserNotExit();
                }
            }
        });
    }

    public interface LoginCallback extends BizCallback<User> {
        public void onLoginSuccess(User user);
        public void onUserNotExit();
        public void onLoginFail(String code);
    }
}
