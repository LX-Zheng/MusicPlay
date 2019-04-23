package com.example.apple.mytable.SQLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apple.mytable.Model.History;

import java.util.ArrayList;

public class HistoryOperate {
    private SQLiteDatabase db;

    public HistoryOperate(Context context){
        MyDatabaseHepler myDatabaseHepler = new MyDatabaseHepler(context, "MyTable", null, 1);
        db = myDatabaseHepler.getWritableDatabase();
    }

    /**
     * 向数据库history中添加历史记录
     * @param tablename 表的名字
     * @param record 搜索的记录
     */
    public void insert(String tablename,String record){
        ContentValues values = new ContentValues();
        values.put("record",record);
        db.insertWithOnConflict(tablename,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 查询history表
     * @param tablename 表的名字
     */
    public ArrayList<History> query(String tablename){
        ArrayList<History> rescrods = new ArrayList<History>();
        Cursor cursor = db.query(tablename,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String record = cursor.getString(cursor.getColumnIndex("record"));
                History history = new History();
                history.setName(record);
                rescrods.add(history);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return rescrods;
    }

    /**
     *删除一条数据
     * @param tablename 表的名字
     * @param record 搜索的记录
     */
    public void delete(String tablename,String record){
        db.delete(tablename,"record=?",new String[]{record});
    }

    /**
     * 清空数据表history
     */
    public void deleteTable(){
        db.execSQL("delete from history");
    }
}
