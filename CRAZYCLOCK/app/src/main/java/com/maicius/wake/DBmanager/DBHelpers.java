package com.maicius.wake.DBmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maicius on 2016/12/21.
 */
public class DBHelpers extends SQLiteOpenHelper {
    private static final String DB_NAME = "clock.db";
    private static final int VERSION = 3;
    public DBHelpers(Context context){
        super(context, DB_NAME, null, VERSION);

    }
    public void onCreate(SQLiteDatabase db){
        String sql = "create table sqlUser(\n" +
                "\tusername varchar(255) primary key,\n" +
                "\tpassword varchar(255),\n" +
                "\tnickname varchar(255)\n" +
                ")";
        db.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS sqlUser");
        onCreate(db);
    }

}
