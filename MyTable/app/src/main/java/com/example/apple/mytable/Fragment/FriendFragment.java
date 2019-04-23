package com.example.apple.mytable.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.mytable.Friend.Search;
import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.Operate;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.TableOperate;
import com.example.apple.mytable.SQLite.UserOperate;
import com.example.apple.mytable.adapter.FriendAdpater;
import com.example.apple.mytable.adapter.PinyinComparator;
import com.example.apple.mytable.adapter.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class FriendFragment extends Fragment {
    private ArrayList<Friend> friendList = new ArrayList<Friend>();
    private FriendAdpater adapter;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SideBar sideBar;
    private TextView dialog;
    private final int REFRESH = 1;
    //根据拼音排序
    private PinyinComparator pinyinComparator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_friend,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        sideBar = (SideBar)view.findViewById(R.id.sideBar);
        dialog = (TextView)view.findViewById(R.id.dialog);
        //toolbar
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar_title = (TextView)view.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
        toolbar_title.setText("联系人");

        Message message = new Message();
        message.what = REFRESH;
        handler.sendMessage(message);

        //刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFreiend();
            }
        });
        return view;
    }

    //刷新
    public void refreshFreiend() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    swipeRefreshLayout.setRefreshing(false);
                    UserOperate userOperate = new UserOperate(getActivity());
                    User user = userOperate.query("user");
                    Operate operate = new Operate(getActivity());
                    operate.refresh(user.getId()+"");
                    Message message = new Message();
                    message.what = REFRESH;
                    handler.sendMessage(message);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //添加自定义toobar布局
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friend_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.friend_toolbar:
                Intent intent = new Intent(getActivity(),Search.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case REFRESH:
                    friendList.clear();
                    pinyinComparator = new PinyinComparator();
                    TableOperate tableOperate = new TableOperate(getActivity());
                    friendList = tableOperate.query("friend");
                    //添加数据run
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    sideBar.setTextView(dialog);
                    sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                        @Override
                        public void onTouchingLetterChanged(String s) {
                            int position = adapter.getPositionForSection(s.charAt(0));
                            if(position != -1){
                                layoutManager.scrollToPositionWithOffset(position,0);
                            }
                        }
                    });
                    Collections.sort(friendList,pinyinComparator);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new FriendAdpater(friendList);
                    recyclerView.setAdapter(adapter);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            Message message = new Message();
            message.what = REFRESH;
            handler.sendMessage(message);
        }
    }

}