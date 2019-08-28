package com.example.myapp01;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    // Flag
    int mMode;
    // 绑定的客户端接口
    IBinder mBinder;
    // Flag
    boolean mAllowRebind;

    public MyService() {
    }

    // Service创建时调用
    @Override
    public void onCreate() {
        Log.d("MyService", "onCreate()");
    }

    // startService()调用时的回调
    @Override
    public int onStartCommand(Intent intent, int flags, int started) {
        Toast.makeText(this, "服务已启动", Toast.LENGTH_LONG).show();
        Log.d("MyService", "onStartCommand()");
        return mMode;
    }

    // 通过 bindService()绑定到客户端
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService", "onBind()");
        return mBinder;
    }

    // unbindService()解除绑定时调用
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService", "onUnbind()");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("MyService", "onRebind()");
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "onDestroy()");
    }
}
