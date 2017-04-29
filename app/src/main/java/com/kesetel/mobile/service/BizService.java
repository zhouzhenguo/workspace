package com.kesetel.mobile.service;


import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kesetel.mobile.receiver.NetStateBroadcastReceiver;
import com.example.common.utils.AppUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BizService extends Service {
    private ScheduledExecutorService executorService = null;
    private NetStateBroadcastReceiver netStateReceiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadScheduledExecutor();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netStateReceiver = new NetStateBroadcastReceiver();
        registerReceiver(netStateReceiver, filter);
    }

    public void registerListener(NetStateBroadcastReceiver.NetStateListener listener){
        netStateReceiver.registerListener(listener);
    }

    public void unregisterListener(NetStateBroadcastReceiver.NetStateListener listener){
        netStateReceiver.unregisterListener(listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if(!AppUtils.isServiceRunning(getApplicationContext(), "com.kesetel.mobile.service.GuardService")){
                    Intent newIntent = new Intent(getApplicationContext(), GuardService.class);
                    startService(newIntent);
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netStateReceiver);
    }
}
