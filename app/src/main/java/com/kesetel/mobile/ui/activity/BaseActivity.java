package com.kesetel.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.kesetel.mobile.present.BasePresent;

/**
 * Created by Administrator on 2017/4/24.
 */

public abstract class BaseActivity<T extends BasePresent> extends FragmentActivity {
    protected T present;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = getContentView();
        if(contentView == null)
            setContentView(getLayoutResId());
        else
            setContentView(contentView);

        present = initPresent();
        initView();
    }

    public View getContentView(){
        return null;
    }

    public abstract int getLayoutResId();

    public abstract T initPresent();

    public abstract void initView();
}
