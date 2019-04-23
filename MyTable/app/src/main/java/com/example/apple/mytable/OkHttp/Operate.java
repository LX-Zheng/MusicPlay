package com.example.apple.mytable.OkHttp;

import android.app.Activity;
import android.widget.Toast;

import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.Model.Library;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.LibraryOperate;
import com.example.apple.mytable.SQLite.TableOperate;
import com.example.apple.mytable.adapter.PinyinUtils;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Operate {

    private Activity activity;
    public Operate(Activity activity){
        this.activity = activity;
    }

    /**
     * 更新好友列表并写入到SQLite中
     */
    public void refresh(String id){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",id)
                .build();
        MyHttp myHttp = new MyHttp(activity);
        myHttp.post("/getfriend", requestBody, new MyHttpCallback() {
            @Override
            public void uiRunnable(String res) {
                try {
                    JSONArray jsonArray = new JSONArray(res.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        String str;
                        String pinyin = PinyinUtils.getPingYin(jsonArray.getJSONObject(i).getString("memoname"));
                        String sortString  = pinyin.substring(0,1).toUpperCase();
                        if(sortString.matches("[A-Z]")){
                            str = sortString.toUpperCase();
                        }else {
                            str = "#";
                        }
                        int imageId = R.drawable.ic_head;
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String id = jsonArray.getJSONObject(i).getString("friend");
                        String memoname = jsonArray.getJSONObject(i).getString("memoname");
                        Friend friend = new Friend(imageId,name,id,str,memoname);
                        TableOperate tableOperate = new TableOperate(activity);
                        tableOperate.insert("friend",friend);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获得图书馆信息
     */
    public void getLibrary(String id){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",id)
                .build();
        MyHttp myHttp = new MyHttp(activity);
        myHttp.post("/getLibrary", requestBody, new MyHttpCallback() {
            @Override
            public void uiRunnable(String res) {
                try {
                    JSONArray jsonArray = new JSONArray(res.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        String name = jsonArray.getJSONObject(i).getString("library");
                        String floor = jsonArray.getJSONObject(i).getString("floor");
                        String detail = jsonArray.getJSONObject(i).getString("detail");
                        Library library = new Library();
                        library.setName(name);
                        library.setFloor(floor);
                        library.setDetail(detail);
                        LibraryOperate libraryOperate = new LibraryOperate(activity);
                        libraryOperate.insert("library",library);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除好友并在数据库中删除记录
     */
    public void deleteFriend(final String id){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",id)
                .build();
        MyHttp myHttp = new MyHttp(activity);
        myHttp.post("/deleteFreind", requestBody, new MyHttpCallback() {
            @Override
            public void uiRunnable(String res) {
                if(res.equals("1")){
                    TableOperate tableOperate = new TableOperate(activity);
                    tableOperate.delete("friend",id);
                    Toast.makeText(activity,"删除成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(activity,"服务器为响应",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
