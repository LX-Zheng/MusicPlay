package com.example.apple.mytable.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.mytable.Home.LibraryDetail;
import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.adapter.HomeAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private HomeAdapter homeAdapter;
    private ArrayList<Friend> arrayList = new ArrayList<Friend>();
    private RecyclerView recyclerView;
    private TextView library;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        library = (TextView)view.findViewById(R.id.library);

        library.setOnClickListener(this);

        //设置toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("");
        toolbar_title.setText("大厅");
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        new Thread(){
            @Override
            public void run(){
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }.start();
        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    RequestBody requestBody = new FormBody.Builder()
                            .add("library","常熟理工学院")
                            .build();
                    MyHttp myHttp = new MyHttp(getActivity());
                    myHttp.post("/getUsers", requestBody, new MyHttpCallback() {
                        @Override
                        public void uiRunnable(String res) {
                            try {
                                JSONArray jsonArray = new JSONArray(res.toString());
                                for(int i=0;i<jsonArray.length();i++){
                                    int imageId = R.drawable.ic_head;
                                    String id = jsonArray.getJSONObject(i).getString("id");
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    Friend friend = new Friend();
                                    friend.setName(name);
                                    friend.setId(id);
                                    friend.setImageId(imageId);
                                    arrayList.add(friend);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                homeAdapter = new HomeAdapter(arrayList);
                                recyclerView.setAdapter(homeAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.library:
                Intent libraryIntent = new Intent(getActivity(),LibraryDetail.class);
                startActivity(libraryIntent);

        }
    }
}
