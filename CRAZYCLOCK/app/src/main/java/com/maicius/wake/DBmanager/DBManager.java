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
    public void insertSQL(RecordingUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into enableRecording(username, enableSleep) values(?,?)",
                    new Object[]{user.getUsername(),
                            user.getEnableSleep()});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }
    /*向sqlUsr表中插入值*/
    public void insertSQL(AppUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into appUser(username, password, nickname) values(?,?,?)",
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
    public void insertSQL(ScreenUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into sleepTime(username, cur_time)" +
                            " values(?,?)",
                    new Object[]{user.getUsername(),
                            user.getCurTime()});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }

    public void insertSQL(GetUpUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into getUpTime(username, get_up_time)" +
                            " values(?,?)",
                    new Object[]{user.getUsername(),
                            user.getGetUpTime()});
            db.setTransactionSuccessful();
        }catch(SQLException E){
            E.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }
    public void insertSQL(SleepUser user){
        db.beginTransaction();
        try{
            db.execSQL("insert into sleep(username, sleep)" +
                            " values(?,?)",
                    new Object[]{user.getSleepUsername(),
                            user.getSleepTime()});
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
            deleteAppUser("appUser");
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
    public void deleteData(String table, String col, String key){
        String sql = "delete from " + table +" where "+col+" =key";
    }
    public void close(){
        db.close();
    }

    public Cursor query(String table){
        Cursor c = db.rawQuery("select * from "+table+"", null);
        return c;
    }
    public Cursor query(String col, String table){
        Cursor c = db.rawQuery("select "+col+" from "+table+"", null);
        return c;
    }

}
