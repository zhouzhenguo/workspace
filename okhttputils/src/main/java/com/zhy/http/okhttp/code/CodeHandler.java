package com.zhy.http.okhttp.code;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface CodeHandler {
    void onCode(int statusCode, Response response);
}
