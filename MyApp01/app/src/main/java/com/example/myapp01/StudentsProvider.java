package com.example.myapp01;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import android.util.Log;

public class StudentsProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.rvrta.example";
    static final String URL = "content://" + PROVIDER_NAME + "/students";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String GRADE = "grade";

    private static HashMap<String, String> STUDENT_PROJECTION_MAP;

    static final int STUDENTS = 1;
    static final int STUDENT_ID = 2;

    static final UriMatcher mMatcher;
    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(PROVIDER_NAME, "students", STUDENTS);
        mMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }

    // DB
    private SQLiteDatabase mDb;
    static final String DB_NAME = "College";
    static final String DB_TABLE_STUDENT = "students";
    static final int DB_VERSION = 1;
    static final String DB_CREATE_TABLE = "CREATE TABLE " + DB_TABLE_STUDENT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL,"+ "grade TEXT NOT NULL);";

    // db helper class
    private static class DbHelper extends SQLiteOpenHelper {
        DbHelper(Context ctx) {
            super(ctx, DB_NAME, null, DB_VERSION);
        }

        /*
        * SQLiteOpenHelper会自动检测数据库文件是否存在。如果存在，会打开这个数据库，在这种情况下就不会调用onCreate()方法。
        * 如果数据库文件不存在，SQLiteOpenHelper首先会创建一个数据库文件，然后打开这个数据库，最后调用onCreate()方法。
        * 因此，onCreate()方法一般用来在新创建的数据库中建立表、视图等数据库组建。也就是说onCreate()方法在数据库文件第一次创建时调用。
        */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE);
        }

        /**
         * 1.如果数据库文件不存在，SQLiteOpenHelper在自动创建数据库后会调用onCreate()方法，在该方法中一般需要创建表、视图等组件。
         *   在创建前数据库一般是空的，因此不需要先删除数据库中相关的组件。
         * 2.如果数据库文件存在，并且当前版本号高于上次创建或升级的版本号，SQLiteOpenHelper会调用onUpgrade()方法，调用该方法后会更新数据库的版本号。
         *   在onUpgrade()方法中除了创建表、视图等组件外，还需要先删除这些相关的组件，因此，在调用onUpgrade()方法前，数据库是存在的，里面还原许多数据库组建。
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oVersion, int nVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STUDENT);
            onCreate(db);
        }
    }

    public StudentsProvider() {
        Log.d("StudentProvider", "Constructor");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int n = 0;
        switch(mMatcher.match(uri)) {
            case STUDENTS:
                n = mDb.delete(DB_TABLE_STUDENT, selection, selectionArgs);
                break;
            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);
                n = mDb.delete(DB_TABLE_STUDENT, _ID + "=" + id +
                        (!TextUtils.isEmpty(selection)?" AND (" + selection + ")": ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("未知URI：" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return n;
    }

    @Override
    public String getType(Uri uri) {
        switch (mMatcher.match(uri)) {
            case STUDENTS:
                return "vnd.android.cursor.dir/vnd.rvrta.students";
            case STUDENT_ID:
                return "vnd.android.cursor.item/vnd.rvrta.students";
            default:
                throw new IllegalArgumentException("不支持的URI：" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // add
        long id = mDb.insert(DB_TABLE_STUDENT, "", values);
        if (id > 0) { // success
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("添加记录失败");
    }

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        DbHelper dbHelper = new DbHelper(ctx);

        // 不存在就创建
        mDb = dbHelper.getWritableDatabase();

        return (null != mDb) ? true:false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE_STUDENT);

        switch (mMatcher.match(uri)) {
            case STUDENTS:
                qb.setProjectionMap(STUDENT_PROJECTION_MAP);
                break;
            case STUDENT_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("未知URI：" + uri);
        }

        if (null == sortOrder || "" == sortOrder) {
            sortOrder = NAME;
        }

        Cursor c = qb.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        //注册内容URI变化的监听器
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int n = 0;
        switch(mMatcher.match(uri)) {
            case STUDENTS:
                n = mDb.update(DB_TABLE_STUDENT, values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                n = mDb.update(DB_TABLE_STUDENT, values, _ID + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection)?" AND (" + selection + ")":""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("未知URI：" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return n;
    }
}
