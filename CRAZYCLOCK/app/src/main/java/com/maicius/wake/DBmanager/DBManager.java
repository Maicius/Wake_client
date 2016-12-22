package com.maicius.wake.DBmanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maicius on 2016/12/21.
 */
public class DBManager{
    private DBHelpers helper;
    private SQLiteDatabase db;
    public DBManager(Context context){
        helper = new DBHelpers(context);
        db = helper.getWritableDatabase();
    }
    /*插入值*/
    public void insertSQL(Appuser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into sqlUser(username, password, nickname) values(?,?,?)",
                    new Object[]{user.sqlusername, user.sqlpassword, user.sqlnickname});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }
    public void updateAppUser(Appuser user){
        db.beginTransaction();
        try{
            db.execSQL("delete from sqlUser where 1=1");
            insertSQL(user);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
    public void deleteAppuser(){
        db.execSQL("delete from sqlUser where 1=1");
    }
    public void close(){
        db.close();
    }

    public Cursor query(){
        Cursor c = db.rawQuery("select * from sqlUser", null);
        return c;
    }
}
