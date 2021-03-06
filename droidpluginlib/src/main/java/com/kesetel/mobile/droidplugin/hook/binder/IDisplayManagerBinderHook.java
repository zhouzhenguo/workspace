package com.kesetel.mobile.droidplugin.hook.binder;

import android.content.Context;
import android.os.IBinder;

import com.kesetel.mobile.droidplugin.hook.BaseHookHandle;
import com.kesetel.mobile.droidplugin.hook.handle.IDisplayManagerHookHandle;
import com.kesetel.mobile.droidplugin.reflect.FieldUtils;
import com.kesetel.mobile.droidplugin.reflect.MethodUtils;
import com.kesetel.mobile.helper.compat.IDisplayManagerCompat;

/**
 * IDisplayManagerBinderHook
 *
 * @author Liu Yichen
 * @date 16/6/13
 */
public class IDisplayManagerBinderHook extends BinderHook {

    private static final String TAG = IDisplayManagerBinderHook.class.getSimpleName();
    private static final String SERVICE_NAME = Context.DISPLAY_SERVICE;

    public IDisplayManagerBinderHook(Context hostContext) {
        super(hostContext);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    Object getOldObj() throws Exception {
        IBinder iBinder = MyServiceManager.getOriginService(SERVICE_NAME);
        return IDisplayManagerCompat.asInterface(iBinder);
    }

    @Override
    protected void onInstall(ClassLoader classLoader) throws Throwable {
        super.onInstall(classLoader);
        Class displayManagerGlobalClass = Class.forName("android.hardware.display.DisplayManagerGlobal");
        Object displayManagerGlobal = MethodUtils.invokeStaticMethod(displayManagerGlobalClass, "getInstance");
        FieldUtils.writeField(displayManagerGlobal, "mDm", MyServiceManager.getProxiedObj(SERVICE_NAME));
    }

    @Override
    protected BaseHookHandle createHookHandle() {
        return new IDisplayManagerHookHandle(mHostContext);
    }

}
