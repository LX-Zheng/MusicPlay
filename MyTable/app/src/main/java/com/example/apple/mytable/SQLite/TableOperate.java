package com.example.apple.mytable.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apple.mytable.Model.Friend;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class TableOperate {
    private MyDatabaseHepler myDatabaseHepler;
    private SQLiteDatabase db;

    public TableOperate(Context context){
        myDatabaseHepler = new MyDatabaseHepler(context,"MyTable.db",null,1);
        db = myDatabaseHepler.getWritableDatabase();
    }

    /**
     * 向数据库中添加friend
     * @param tablename 数据库插入的数据表
     * @param object 数据库插入的对象
     */
    public void insert(String tablename,Object object){
        //getClass()方法获得一个实例的类型
        Class cla = object.getClass();
        //获得该类的所有属性 反射
        Field[] fields = cla.getDeclaredFields();
        ContentValues values = new ContentValues();
        for (Field field:fields){
            try{
                field.setAccessible(true);
                if(field.get(object) instanceof Integer){
                    Integer content = (Integer)field.get(object);
                    values.put(field.getName(),content);
                }else {
                    String content = (String)field.get(object);
                    values.put(field.getName(),content);
                }
                field.setAccessible(false);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        db.insertWithOnConflict(tablename,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 查询数据库
     * @param tablename 数据表的名字
     */
    public ArrayList<Friend> query(String tablename){
        ArrayList<Friend> arrayList = new ArrayList<>();
        Cursor cursor = db.query(tablename,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Friend friend = new Friend();
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String ImageId = cursor.getString(cursor.getColumnIndex("imageId"));
                String Name = cursor.getString(cursor.getColumnIndex("name"));
                String Letters = cursor.getString(cursor.getColumnIndex("letters"));
                String memoname = cursor.getString(cursor.getColumnIndex("memoname"));
                friend.setId(id);
                friend.setImageId(parseInt(ImageId));
                friend.setName(Name);
                friend.setLetters(Letters);
                friend.setMemoname(memoname);
                arrayList.add(friend);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * 修改数据库
     * @param tablename 数据表名字
     * @param memoname 用户的备注名
     * @param id 用户的id
     */
    public void update(String tablename,String memoname,String id){
        ContentValues values = new ContentValues();
        values.put("memoname",memoname);
        db.update(tablename,values,"id=?",new String[]{id});
    }

    /**
     * 删除friend
     * @param tablename 数据表的名字
     * @param id 要删除用户的id
     */
    public void delete(String tablename,String id){
        db.delete(tablename,"id=?",new String[]{id});
    }
}
