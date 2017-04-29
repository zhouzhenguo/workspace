package com.kesetel.mobile.helper;

import android.content.Context;

import com.example.common.utils.AppUtils;
import com.google.gson.JsonObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.code.CodeHandler;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Http请求帮助类，统一的请求入口
 * Created by Administrator on 2017/4/27.
 */

public class HttpHelper {
    private static Map<String, String> noBizParams;
    private static final int connectTimeOut = 10000;

    /**
     * http 模块的初始化操作
     * @param context
     */
    public static void init(Context context) {
        initNoBizParams(context);
        OkHttpUtils httpUtils = OkHttpUtils.initClient(null);
        httpUtils.registerCodeHandler(500, new CodeHandler() {
            @Override
            public void onCode(int statusCode, Response response) {

            }
        });
    }

    /**
     * 初始化http请求中 非业务参数
     */
    private static void initNoBizParams(Context context) {
        noBizParams = new LinkedHashMap<>();
        noBizParams.put("appVersion", AppUtils.getAppVersionName(context));
    }

    /**
     * 合并http请求 业务逻辑参数和非业务参数
     * @param bizParams  具体请求接口的业务参数
     * @return
     */
    private static Map<String, String> buildParams(Map<String, String> bizParams) {
        Map<String, String> allParams = new LinkedHashMap<>();
        allParams.putAll(noBizParams);
        if (bizParams != null) {
            allParams.putAll(bizParams);
        }
        return allParams;
    }

    /**
     * Get 请求
     * @param url   请求网络url
     * @param bizParams  请求接口的参数
     * @param callback  网络请求后台响应结果后的回调
     */
    public static void get(String url, Map<String, String> bizParams, Callback callback) {
        Map<String, String> allParams = buildParams(bizParams);
        OkHttpUtils
                .get()
                .url(url)
                .params(allParams)
                .build()
                .connTimeOut(connectTimeOut)
                .execute(callback);
    }

    /**
     * Post 请求 -- 提交Post请求body格式为Json字符串
     * @param url
     * @param bizParams
     * @param callback
     */
    public static void postJson(String url, JsonObject bizParams, Callback callback) {
        String content = null;
        String key = null;
        // 拼接非业务接口参数
        Iterator<String> keyIterator = noBizParams.keySet().iterator();
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            bizParams.addProperty(key, noBizParams.get(key));
        }
        content = bizParams.getAsString();

        postJson(url, content, callback);
    }

    public static void postJson(String url, Map<String, String> bizParams, Callback callback) {
        String content = null;
        JsonObject jsonObject = new JsonObject();
        Map<String, String> allParams = buildParams(bizParams);
        Iterator<String> keyIterator = allParams.keySet().iterator();
        String key = null;
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            jsonObject.addProperty(key, allParams.get(key));
        }
        content = jsonObject.getAsString();

        postJson(url, content, callback);
    }

    private static void postJson(String url, String content, Callback callback){
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(content)
                .build()
                .connTimeOut(connectTimeOut)
                .execute(callback);
    }


    public static void postString(String url, String content, Callback callback) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(content)
                .build()
                .connTimeOut(connectTimeOut)
                .execute(callback);
    }

    public static void postFile(String url, File file, Callback callback) {
        if (!file.exists()) {
            return;
        }
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .connTimeOut(connectTimeOut)
                .execute(callback);
    }

    /**
     *
     * @param url
     * @param saveDir
     * @param saveFileName
     * @param callback
     */
    public static void downloadFile(String url, String saveDir, String saveFileName, final FileCallBack callback) {
        final RequestCall call = OkHttpUtils.get()
                .url(url)
                .build();
        call.execute(callback);
    }
}
