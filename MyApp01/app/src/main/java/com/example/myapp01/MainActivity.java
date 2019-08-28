package com.example.myapp01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String mAndroid = "Android: ";

    // members
    Button mBtnClick;

    // onCreate Activity第一次创建时调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取button对象
        mBtnClick = findViewById(R.id.btn_click);
        // 设置click事件
        mBtnClick.setOnClickListener(new BtnClickListener());

        Log.d(mAndroid, "onCreate()");
    }

    // 内部类，实现按钮事件
    class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "点击事件", Toast.LENGTH_SHORT).show();
        }
    }

    // Activity即将可见时调用
    @Override
    protected void onStart() {
        // 先调用父类方法
        super.onStart();

        Log.d(mAndroid, "onStart()");
    }

    // Activity已经可见时调用
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(mAndroid, "onResume()");
    }

    // 其他活动获得焦点时调用
    @Override
    protected void onPause() {
        super.onPause();

        Log.d(mAndroid, "onPause()");
    }

    // 当活动不可见时调用
    @Override
    protected void onStop() {
        super.onStop();

        Log.d(mAndroid, "onStop()");
    }

    // 当活动销毁时调用
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(mAndroid, "onDestroy()");
    }
}
