package com.kesetel.mobile.ui.activity.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.kesetel.mobile.contract.user.LoginContract;
import com.kesetel.mobile.entity.setting.User;
import com.kesetel.mobile.present.BasePresent;
import com.kesetel.mobile.present.user.LoginPersent;
import com.kesetel.mobile.service.GuardService;
import com.kesetel.mobile.ui.activity.test.BSDiffPatchActivity;
import com.kesetel.mobile.ui.activity.BaseActivity;
import com.kesetel.mobile.ui.activity.test.PluginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

public class LoginActivity<T extends LoginContract.ILoginPresent> extends BaseActivity implements LoginContract.ILoginView {
    private TextView textView;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresent initPresent() {
        return new LoginPersent(this);
    }

    @Override
    public void initView() {
        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.netRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginPersent) present).login("zwx", "123456");
            }
        });

        findViewById(R.id.plginTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PluginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.incrementUpgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BSDiffPatchActivity.class);
                startActivity(intent);
            }
        });

        textView.setText("new Version 2.0");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE}, 0);
            }
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        ImageLoader.getInstance().displayImage("http://cdn.duitang.com/uploads/item/201112/24/20111224182942_yF5uK.thumb.600_0.jpg", imageView);

        Intent intent = new Intent(getApplicationContext(), GuardService.class);
        startService(intent);
    }

    @Override
    public void onLoginSuccess(User user) {
        textView.setText(user.username);
    }

    @Override
    public void onUserNotExit() {
        Toast.makeText(this, "onUserNotExit",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFail(String code) {
        if("200".equals(code)){
            Toast.makeText(this, getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Login fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        File file = new File(Environment.getExternalStorageDirectory(), "abcd.txt");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}
