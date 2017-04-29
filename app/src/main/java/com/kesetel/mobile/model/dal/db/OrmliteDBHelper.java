package com.kesetel.mobile.model.dal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kesetel.mobile.entity.setting.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */

public class OrmliteDBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "orm_application.db";
    public static final int DB_VERSION = 1;
    private Map<String, Dao> daoMap = null;
    private static OrmliteDBHelper instance = null;

    private OrmliteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        init();
    }

    private void init(){
        daoMap = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            Log.i("DDDD", "Ormlite Database Created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onCreate(database, connectionSource);
    }

    public synchronized static OrmliteDBHelper getInstance(Context context){
        if(instance == null)
            instance = new OrmliteDBHelper(context);
        return instance;
    }

    public Dao getUserDao(Class clazz){
        String className = clazz.getSimpleName();
        Dao dao = null;
        if(daoMap.containsKey(className)) {
            dao = daoMap.get(className);
        }
        if(dao == null) {
            try {
                dao = getDao(clazz);
                daoMap.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

}
