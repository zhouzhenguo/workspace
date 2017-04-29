package com.kesetel.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.kesetel.mobile.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 * 网络状态改变监听器
 */

public class NetStateBroadcastReceiver extends BroadcastReceiver {
    private List<NetStateListener> netListeners;

    public NetStateBroadcastReceiver(){
        netListeners = new ArrayList<>();
    }

    public void registerListener(NetStateListener listener){
        if(!netListeners.contains(listener))
            netListeners.add(listener);
    }

    public void unregisterListener(NetStateListener listener){
        netListeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "wifiState" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    //TODO Wifi disabled.
                    for(NetStateListener listener : netListeners){
                        listener.onWifiDisabled();
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLING:

                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    //TODO Wifi enabled.
                    for(NetStateListener listener : netListeners){
                        listener.onWifiEnabled();
                    }
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                default:
                    break;
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager
        // .WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
        // 当然刚打开wifi肯定还没有连接到有效的无线
        else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "isConnected" + isConnected);
                if (isConnected) {
                    //TODO Wifi connected.
                    for(NetStateListener listener : netListeners){
                        listener.onNetConnected();
                    }
                } else {
                    //TODO Wifi disconected.
                    for(NetStateListener listener : netListeners){
                        listener.onNetDisconnected();
                    }
                }
            }
        }
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        //TODO Wifi connected.
                        LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "WiFi已连接");
                        for(NetStateListener listener : netListeners){
                            listener.onWifiConnected();
                        }
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        //TODO Mobile connected.
                        for(NetStateListener listener : netListeners){
                            listener.onMobileConnected();
                        }
                        LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "移动网络已连接");
                    }
                } else {
                    // TODO Network disconnected.
                    LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "网络已断开");
                    for(NetStateListener listener : netListeners){
                        listener.onNetDisconnected();
                    }
                }
            } else {   // not connected to the internet
                // TODO Network disabled.
                LogHelper.logFileAndLogcat("NetStateBroadcastReceiver", "没有开启移动网络与WiFi，无可用网络");
                for(NetStateListener listener : netListeners){
                    listener.onNetDisabled();
                }
            }
        }
    }

    public interface NetStateListener {
        void onMobileConnected();
        void onWifiConnected();
        void onWifiEnabled();
        void onWifiDisabled();
        void onNetDisconnected();
        void onNetConnected();
        void onNetDisabled();
    }
}
