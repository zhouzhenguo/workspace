package com.kesetel.mobile.ui.activity.test;

import android.os.Environment;
import android.view.View;

import com.kesetel.mobile.present.BasePresent;
import com.kesetel.mobile.ui.activity.BaseActivity;
import com.github.snowdream.bsdiffpatch.BSDiffPatch;
import com.github.snowdream.diffpatch.IDiffPatch;

import java.io.File;

/**
 * Created by Administrator on 2017/4/26.
 */

public class BSDiffPatchActivity extends BaseActivity {
    String sdcardPath = null;
    String oldApkPath = null;
    String newApkPath = null;
    String patchPath = null;
    String generateApkPath = null;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_bsdiffpatch;
    }

    @Override
    public BasePresent initPresent() {
        return null;
    }

    @Override
    public void initView() {
        sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(sdcardPath+"/aplication");
        if(!file.exists())
            file.mkdir();

        oldApkPath = sdcardPath+"/aplication/old.apk";
        newApkPath = sdcardPath+"/aplication/new.apk";
        patchPath = sdcardPath+"/aplication/patch";
        generateApkPath = sdcardPath+"/aplication/generateNew.apk";

        final IDiffPatch bsDiffPatch = new BSDiffPatch();
        bsDiffPatch.init(getApplicationContext());

        findViewById(R.id.generatePatch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePatch(bsDiffPatch);
            }
        });

        findViewById(R.id.generateNewApk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewApk(bsDiffPatch);
            }
        });

        findViewById(R.id.installApk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void generatePatch(IDiffPatch bsDiffPatch){
        File patchFile = new File(patchPath);
        if(patchFile.exists())
            patchFile.delete();
        bsDiffPatch.diff(oldApkPath, newApkPath, patchPath);
    }

    private void generateNewApk(IDiffPatch bsDiffPatch){
        File generateFile = new File(generateApkPath);
        if(generateFile.exists())
            generateFile.delete();
        bsDiffPatch.patch(oldApkPath, patchPath, generateApkPath);
    }
}
