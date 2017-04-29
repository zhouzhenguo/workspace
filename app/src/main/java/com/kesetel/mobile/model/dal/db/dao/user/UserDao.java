package com.kesetel.mobile.model.dal.db.dao.user;

import android.content.Context;

import com.kesetel.mobile.model.dal.db.OrmliteDBHelper;
import com.kesetel.mobile.entity.setting.User;
import com.kesetel.mobile.helper.LogHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/4/24.
 */

public class UserDao {
    private Dao<User, Integer> userDao;

    public UserDao(Context context){
        OrmliteDBHelper dbHelper = OrmliteDBHelper.getInstance(context);
        userDao = dbHelper.getUserDao(User.class);
    }

    public void insert(User user){
        try {
            userDao.createOrUpdate(user);
            LogHelper.logFileAndLogcat("LoginBiz", "UserDao insert success: "+user.username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
