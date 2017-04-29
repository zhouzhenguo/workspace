package com.zhy.http.okhttp.response;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.code.CodeHandler;
import com.zhy.http.okhttp.utils.Platform;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Http请求后台Response统一处理类
 * Created by Administrator on 2017/4/27.
 */

public class ResponseHandler implements Callback {

    private OkHttpUtils okHttpUtils;
    private com.zhy.http.okhttp.callback.Callback callback;
    private int callId;
    private Platform platform;

    public ResponseHandler(OkHttpUtils okHttpUtils, com.zhy.http.okhttp.callback.Callback callback, int callId, Platform platform) {
        this.okHttpUtils = okHttpUtils;
        this.callback = callback;
        this.callId = callId;
        this.platform = platform;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        sendFailResultCallback(call, e, callback, callId);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            if (call.isCanceled()) {
                sendFailResultCallback(call, new IOException("Canceled!"), callback, callId);
                return;
            }

            // 统一http状态码处理
            Integer code = response.code();
            CodeHandler codeHandler = okHttpUtils.getCodeHandler(code);
            if (codeHandler != null) {
                codeHandler.onCode(code, response);
            }

            if (!callback.validateReponse(response, callId)) {
                sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), callback, callId);
                return;
            }

            Object o = callback.parseNetworkResponse(response, callId);
            sendSuccessResultCallback(o, callback, callId);

        } catch (Exception e) {
            sendFailResultCallback(call, e, callback, callId);
        } finally {
            if (response.body() != null)
                response.body().close();
        }
    }

    public void sendFailResultCallback(final Call call, final Exception e, final com.zhy.http.okhttp.callback.Callback callback, final int id) {
        if (callback == null) return;
        platform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final com.zhy.http.okhttp.callback.Callback callback, final int id) {
        if (callback == null) return;
        platform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }
}
