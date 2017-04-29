package com.kesetel.mobile.entity.setting;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017/4/24.
 */

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(columnName = "username", id = true)
    public String username;
    @DatabaseField(columnName = "password")
    public String password;
}
