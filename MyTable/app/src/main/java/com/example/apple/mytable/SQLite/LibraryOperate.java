package com.example.apple.mytable.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apple.mytable.Model.Library;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LibraryOperate {
    private SQLiteDatabase db;

    public LibraryOperate(Context context){
        MyDatabaseHepler myDatabaseHepler = new MyDatabaseHepler(context, "MyTable", null, 1);
        db = myDatabaseHepler.getWritableDatabase();
    }

    /**
     * 向数据库中添加图书馆信息
     * @param tablename 表的名称
     * @param object 插入的对象
     */
    public void insert(String tablename,Object object){
        Class cla = object.getClass();
        Field[] fields = cla.getDeclaredFields();
        ContentValues values = new ContentValues();
        for(Field field:fields){
            try{
                field.setAccessible(true);
                String content = (String)field.get(object);
                values.put(field.getName(),content);
                field.setAccessible(false);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        db.insertWithOnConflict(tablename,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 查询图书馆信息
     * @param tablename 表的名字
     */
    public ArrayList<Library> query(String tablename){
        ArrayList<Library> arrayList = new ArrayList<Library>();
        Cursor cursor = db.query(tablename,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String floor = cursor.getString(cursor.getColumnIndex("floor"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                Library library = new Library();
                library.setFloor(floor);
                library.setDetail(detail);
                arrayList.add(library);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }
}
