package com.zyh.blocknotifications.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Time:2019/4/21
 * Author:ZYH
 * Description:
 */
public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;

    public DbHelper(Context context) {
        super(context, "info.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS info (id integer primary key autoincrement,package text,title text,content text,time text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }
}
