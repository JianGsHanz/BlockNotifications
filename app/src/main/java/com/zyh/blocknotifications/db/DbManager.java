package com.zyh.blocknotifications.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zyh.blocknotifications.model.Info;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Time:2019/4/21
 * Author:ZYH
 * Description:
 */
public class DbManager {

    private DbHelper helper;
    private SQLiteDatabase database;

    public DbManager(Context context) {
        helper = DbHelper.getInstance(context);
        database = helper.getWritableDatabase();
    }

    public void insert(String packa,String title,String content){
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        String sql = "insert into info(package,title,content,time) values('"+packa+"','"+title+"','"+content+"','"+sdf.format(new Date(System.currentTimeMillis()))+"')";
        database.execSQL(sql);
    }
    public ArrayList<Info> query(){
        ArrayList<Info> infos = new ArrayList<>();
        String sql = "select package,title,content,time from info";
        Cursor cursor = database.rawQuery(sql, null);
        for (;cursor.moveToNext();){
            Info info = new Info();
            info.setPacka(cursor.getString(cursor.getColumnIndex("package")));
            info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            info.setContent(cursor.getString(cursor.getColumnIndex("content")));
            info.setTime(cursor.getString(cursor.getColumnIndex("time")));
            infos.add(info);
        }
        return infos;
    }

}
