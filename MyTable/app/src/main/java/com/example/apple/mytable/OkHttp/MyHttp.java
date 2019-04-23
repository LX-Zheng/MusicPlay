package com.example.apple.mytable.OkHttp;


import android.app.Activity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyHttp {
    private Activity activity;
    public static String host = "http://120.77.203.242:3000";

    public MyHttp(String host,Activity activity){
        this.host = host;
        this.activity = activity;
    }

    public MyHttp(Activity activity){
        this.activity = activity;
    }

    /**
     * 发送get请求
     * @param url 请求地址
     * @param callback 回调函数
     */
    public void get(String url, final MyHttpCallback callback){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(host + url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //写错误处理
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code() == 200){
                            callback.uiRunnable(res);
                        }
                        //写错误处理
                    }
                });
            }
        });
    }

    /**
     * 发送POST请求
     * @param url 请求地址
     * @param requestBody 请求的内容
     * @param callback 回调函数
     */
    public void post(String url, RequestBody requestBody, final MyHttpCallback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(host + url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //写错误处理
                    }
                });
            }

            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                final String res = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code() == 200){
                            callback.uiRunnable(res);
                        }
                        //写错误处理
                    }
                });
            }
        });
    }
}