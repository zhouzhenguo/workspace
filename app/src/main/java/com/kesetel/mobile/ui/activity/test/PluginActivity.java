package com.kesetel.mobile.ui.activity.test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.kesetel.mobile.present.BasePresent;
import com.kesetel.mobile.ui.activity.BaseActivity;
import com.kesetel.mobile.helper.LogHelper;
import com.kesetel.mobile.droidplugin.pm.PluginManager;

import static com.kesetel.mobile.helper.compat.PackageManagerCompat.INSTALL_FAILED_NOT_SUPPORT_ABI;
import static com.kesetel.mobile.helper.compat.PackageManagerCompat.INSTALL_SUCCEEDED;

/**
 * Created by Administrator on 2017/4/25.
 */

public class PluginActivity extends BaseActivity implements ServiceConnection {
    private Handler handler = new Handler();
    private PackageManager pm = null;
    final String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/aplication/plugin.apk";
    PackageInfo packageInfo = null;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public BasePresent initPresent() {
        return null;
    }

    @Override
    public void initView() {
        pm = getPackageManager();
        packageInfo = pm.getPackageArchiveInfo(pluginPath, 0);
        PluginManager.getInstance().addServiceConnection(this);

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PluginManager.getInstance().isConnected()) {
                    Toast.makeText(PluginActivity.this, "插件服务正在初始化，请稍后再试。。。", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (PluginManager.getInstance().getPackageInfo(packageInfo.packageName, 0) != null) {
                        Toast.makeText(PluginActivity.this, "已经安装了，不能再安装", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    final int re = PluginManager.getInstance().installPackage(pluginPath, 0);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch (re) {
                                                case PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION:
                                                    Toast.makeText(PluginActivity.this, "安装失败，文件请求的权限太多", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case INSTALL_FAILED_NOT_SUPPORT_ABI:
                                                    Toast.makeText(PluginActivity.this, "宿主不支持插件的abi环境，可能宿主运行时为64位，但插件只支持32位", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case INSTALL_SUCCEEDED:
                                                    Toast.makeText(PluginActivity.this, "安装完成", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }

                                        }
                                    });
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        PluginManager.getInstance().installPackage(pluginPath, 0);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.openPlugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.openPluginService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.zzg.pluginservice");
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PluginManager.getInstance().removeServiceConnection(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LogHelper.logFileAndLogcat("DDDDDD", "PluginActivity bind plugin service success.");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogHelper.logFileAndLogcat("DDDDDD", "PluginActivity bind plugin service onServiceDisconnected.");
    }
}
