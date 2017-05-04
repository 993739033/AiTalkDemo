package com.app.aitalkdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 知らないのセカイ on 2017/5/4.
 */

public class MyData extends SQLiteOpenHelper {
    private Context mcontent;
    public final static String CODE="code",TEXT="text",URL="url",TYPE="type",INFO="info",DATE="date",NAME="msg";
    private final static String CREATE_TABLE="create table msg("+
            "id integer primary key autoincrement,"+
            "code integer,"+
            "text text,"+
            "url text,"+
            "type integer,"+
            "info text,"+
            "date text)";

    public MyData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontent=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_TABLE);
        Toast.makeText(mcontent, "database created!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
