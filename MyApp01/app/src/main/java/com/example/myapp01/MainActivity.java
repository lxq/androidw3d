package com.example.myapp01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.util.Log;

import android.view.Menu;
import android.net.Uri;

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

    // start service
    public void startService(View v) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // stop service
    public void stopService(View v) {
        stopService(new Intent(getBaseContext(), MyService.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate();
        return true;
    }

    public void onBtnAdd(View v) {
        ContentValues cv = new ContentValues();
        cv.put(StudentsProvider.NAME, ((EditText)findViewById(R.id.etName)).getText().toString());
        cv.put(StudentsProvider.GRADE, ((EditText)findViewById(R.id.etGrade)).getText().toString());

        ContentResolver resolver = getContentResolver();
        Uri uri = resolver.insert(StudentsProvider.CONTENT_URI, cv);

        Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onBtnQuery(View v) {
        Uri uri = Uri.parse(StudentsProvider.URL);
        // managedQuery() 已经不再使用.
        Cursor c = getContentResolver().query(uri,null,null,null,"name");

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,c.getString(c.getColumnIndex(StudentsProvider._ID)) + ","
                        + c.getString(c.getColumnIndex(StudentsProvider.NAME)) + ","
                        + c.getString(c.getColumnIndex(StudentsProvider.GRADE)), Toast.LENGTH_LONG).show();
            }while (c.moveToNext());
        }
    }
}
