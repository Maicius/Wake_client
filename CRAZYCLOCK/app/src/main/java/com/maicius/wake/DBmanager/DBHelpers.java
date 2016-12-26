package com.maicius.wake.DBmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maicius on 2016/12/21.
 */
public class DBHelpers extends SQLiteOpenHelper {
    private static final String DB_NAME = "clock.db";
    private static final int VERSION = 1;
    public DBHelpers(Context context){
        super(context, DB_NAME, null, VERSION);

    }
    public void onCreate(SQLiteDatabase db){
        String sql = "create table appUser(\n" +
                "\tusername varchar(255) primary key,\n" +
                "\tpassword varchar(255),\n" +
                "\tnickname varchar(255)\n" +
                ")";
        db.execSQL(sql);
        sql = "create table sleepTime(\n" +
                "\tsleep_time_id int,\n" +
                "\tusername varchar(255),\n" +
                "\tcur_time varchar(255),\n" +
                "\tprimary key(sleep_time_id),\n" +
                "\tforeign key(username) references appUser(username)\n" +
                ")";
        db.execSQL(sql);
        sql = "create table getUpTime(\n" +
                "\tget_up_time_id int primary key,\n" +
                "\tusername varchar(255),\n" +
                "\tget_up_time varchar(255),\n" +
                "\tforeign key(username) references appUser(username)\n" +
                ")";
        db.execSQL(sql);
        sql = "create table sleep(\n" +
                "\tsleep_id int,\n" +
                "\tusername varchar(255),\n" +
                "\tsleep numeric(4,2),\n" +
                "\tprimary key(sleep_id),\n" +
                "\tforeign key(username) references appUser(username)\n" +
                ")";
        db.execSQL(sql);
        sql = "create table enableRecording(\n" +
                "\tenable_id int primary key,\n" +
                "\tusername varchar(255),\n" +
                "\tscreenOffTime int,\n" +
                "\tforeign key(username) references appUser(username)\n" +
                ")";
        db.execSQL(sql);
        sql = "create table greeting(\n" +
                "\tgreeting_id int,\n" +
                "\treceive_user varchar(255),\n" +
                "\tsend_user varchar(255),\n" +
                "\tgreeting_text varchar(255),\n" +
                "\tforeign key(receive_user) references appUser(username),\n" +
                "\tprimary key(greeting_id)\n" +
                ")";
        db.execSQL(sql);
        sql ="create table screenOffTime(\n" +
                "    time_id int,\n" +
                "    username varchar(255),\n" +
                "    screenOffTime varchar(255),\n" +
                "    primary key(time_id)\n" +
                ")";
        db.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS greeting");
        db.execSQL("DROP TABLE IF EXISTS enableRecording");
        db.execSQL("DROP TABLE IF EXISTS sleepTime");
        db.execSQL("DROP TABLE IF EXISTS getUpTime");
        db.execSQL("DROP TABLE IF EXISTS sleep");
        db.execSQL("DROP TABLE IF EXISTS appUser");
        onCreate(db);
    }

}
