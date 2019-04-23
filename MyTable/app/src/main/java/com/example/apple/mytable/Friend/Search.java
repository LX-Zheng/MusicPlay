package com.example.apple.mytable.Friend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.Model.History;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.HistoryOperate;
import com.example.apple.mytable.adapter.HistoryAdapter;
import com.example.apple.mytable.adapter.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Search extends AppCompatActivity implements View.OnClickListener {

    public final int SERACH = 1;
    public final int reload = 2;
    private SearchView search_txt;
    private RecyclerView recyclerView;
    private List<Friend> users;
    private HistoryOperate historyOperate;
    private LinearLayoutManager layoutManager;
    private TextView clear;

    public void bind(){
        search_txt = (SearchView) findViewById(R.id.search_txt);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        clear = (TextView)findViewById(R.id.clear);
        historyOperate = new HistoryOperate(Search.this);
        layoutManager = new LinearLayoutManager(Search.this);

        clear.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bind();
        search_txt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("context",s);
                message.setData(bundle);
                message.what = SERACH;
                handler.sendMessage(message);
                historyOperate.insert("history",s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    showHistory();
                }
                return false;
            }
        });
        showHistory();
    }

    //show搜索结果
    public void show(List<Friend> users){
        List<Friend> data = new ArrayList<>();
        for(int i=0;i<users.size();i++){
            data.add(new Friend(R.mipmap.image,users.get(i).getName(),users.get(i).getId()));
        }
        recyclerView.setLayoutManager(layoutManager);
        SearchAdapter adapter = new SearchAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    //show历史记录
    public void showHistory(){
        ArrayList<History> histories;
        histories = historyOperate.query("history");
        Log.d("showHistory: ",histories.size()+"");
        recyclerView.setLayoutManager(layoutManager);
        HistoryAdapter historyAdapter = new HistoryAdapter(histories);
        recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.clear:
                historyOperate.deleteTable();
                Toast.makeText(Search.this,"清空成功",Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = reload;
                handler.sendMessage(message);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case SERACH:
                    String context = message.getData().getString("context");
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",context)
                            .build();
                    MyHttp myHttp = new MyHttp(Search.this);
                    myHttp.post("/search", requestBody, new MyHttpCallback() {
                        @Override
                        public void uiRunnable(String res) {
                            try{
                                users = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(res.toString());
                                for (int i=0;i<jsonArray.length();i++){
                                    Friend friend = new Friend(R.mipmap.image,jsonArray.getJSONObject(i).getString("name"),jsonArray.getJSONObject(i).getString("id"));
                                    users.add(friend);
                                }
                                show(users);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case reload:
                    showHistory();
                    break;
            }
        }
    };
}
