package com.maicius.wake.DBmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
    /*向sqlUsr表中插入值*/
    public void insertSQL(AppUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into sqlUser(username, password, nickname) values(?,?,?)",
                    new Object[]{user.getSqlusername(),
                            user.getSqlpassword(),
                            user.getSqlnickname()});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }
    /*向sleepTime表中插入值*/
    public void insertSQL(SleepTimeUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into sleepTime(username, cur_time)" +
                            " values(?,?,?)",
                    new Object[]{user.getUsername(),
                            user.getCurTime()});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }
    /*更新sqlUser中的值*/
    public void updateAppUser(AppUser user){
        db.beginTransaction();
        try{
            deleteAppUser("sqlUser");
            insertSQL(user);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
    public void deleteAppUser(String table){
        String sql = "delete from " + table +" where 1=1";
        db.execSQL(sql);
    }
    public void close(){
        db.close();
    }

    public Cursor query(){
        Cursor c = db.rawQuery("select * from sqlUser", null);
        return c;
    }
}
