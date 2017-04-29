package com.kesetel.mobile.model.dal;

import com.kesetel.mobile.helper.HttpHelper;
import com.zhy.http.okhttp.callback.Callback;

/**
 * 数据访问层
 * Created by Administrator on 2017/4/28.
 */

public class UserDal {

    public void login(String username, String password, Callback callback){
        HttpHelper.get("http://blog.csdn.net/gsying1474/article/details/70522250", null, callback);
    }
}
