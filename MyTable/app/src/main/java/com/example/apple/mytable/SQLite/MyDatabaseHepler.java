package com.example.apple.mytable.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MyDatabaseHepler extends SQLiteOpenHelper {
    private static final String CREATE_USER = "create table user("
            +"id integer primary key,"
            +"sex integer not null,"
            +"state integer not null,"
            +"remember integer not null,"
            +"account text not null,"
            +"password text not null,"
            +"name text not null,"
            +"library text,"
            +"islogin text not null);";
    private static final String CREATE_FRIEND = "create table friend("
            +"id text primary key,"
            +"imageId integer not null,"
            +"name text not null,"
            +"memoname text not null,"
            +"letters text not null);";
    private static final String CREATE_HISTORY = "create table history("
            +"record text primary key not null);";
    private static final String CREATE_LIBRARY = "create table library("
            +"name text not null,"
            +"floor text primary key not null,"
            +"detail text not null);";
    private Context mContext;

    public MyDatabaseHepler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_FRIEND);
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_LIBRARY);
        Toast.makeText(mContext,"Created Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}

}
