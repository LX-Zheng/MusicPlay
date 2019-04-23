package com.example.apple.mytable.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apple.mytable.Model.User;

public class UserOperate {
    private MyDatabaseHepler myDatabaseHepler;
    private SQLiteDatabase db;

    public UserOperate(Context context){
        myDatabaseHepler = new MyDatabaseHepler(context,"MyTable.db",null,1);
        db = myDatabaseHepler.getWritableDatabase();
    }

    /**
     * 查询user信息
     * @param tablename 查询的表明
     */
    public User query(String tablename){
        Cursor cursor = db.query(tablename,null,null,null,null,null,null);
        User user = new User();
        if(cursor.moveToFirst()){
            do {
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                Integer sex = cursor.getInt(cursor.getColumnIndex("sex"));
                Integer state = cursor.getInt(cursor.getColumnIndex("state"));
                Integer remember = cursor.getInt(cursor.getColumnIndex("remember"));
                String library = cursor.getString(cursor.getColumnIndex("library"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String islogin = cursor.getString(cursor.getColumnIndex("islogin"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                user.setId(id);
                user.setSex(sex);
                user.setRemember(remember);
                user.setAccount(account);
                user.setName(name);
                user.setLibrary(library);
                user.setState(state);
                user.setPassword(password);
                user.setIslogin(islogin);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return user;
    }

    /**
     * @param tablename 修改的数据库名称
     * @param culname 修改的列名
     * @param value 修改的值
     * @param id 用户的id
     */
    public void update(String tablename,String culname,int value,String id){
        ContentValues values = new ContentValues();
        values.put(culname,value);
        db.update(tablename,values,"id=?",new String[]{id});
    }

    /**
     * 修改用户的登录状态
     * @param tablename 修改的数据库名称
     * @param id 用户的id
     */
    public void updateState(String tablename,String id){
        ContentValues values = new ContentValues();
        values.put("islogin","false");
        db.update(tablename,values,"id=?",new String[]{id});
    }

    /**
     * 清空user表
     */
    public void delete(){
        db.execSQL("delete from user");
    }
}
