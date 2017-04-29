package com.kesetel.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.common.utils.AppUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/4/25.
 */

public class GuardService extends Service {
    private ScheduledExecutorService executorService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if(!AppUtils.isServiceRunning(getApplicationContext(), "com.kesetel.mobile.service.BizService")){
                    Intent newIntent = new Intent(getApplicationContext(), BizService.class);
                    startService(newIntent);
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
        return super.onStartCommand(intent, flags, startId);
    }
}
