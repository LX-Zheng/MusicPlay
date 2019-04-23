package com.example.apple.mytable.Home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.apple.mytable.Model.Library;
import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.Operate;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.LibraryOperate;
import com.example.apple.mytable.SQLite.UserOperate;
import com.example.apple.mytable.adapter.LibraryAdapter;

import java.util.ArrayList;

public class LibraryDetail extends AppCompatActivity {

    //UI Object
    private LinearLayout information;
    private RecyclerView recyclerView;
    private LibraryAdapter adapter;
    private UserOperate userOperate;
    private User user;
    private ArrayList<Library> arrayList;

    public void bind(){
        information = (LinearLayout)findViewById(R.id.information);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        userOperate = new UserOperate(this);
        user = new User();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_detail);
        bind();
        //confirmInfo();

        UserOperate userOperate = new UserOperate(this);
        User user = userOperate.query("user");
        Operate operate = new Operate(this);
        operate.getLibrary(user.getId()+"");

        LibraryOperate libraryOperate = new LibraryOperate(this);
        arrayList = libraryOperate.query("library");
        adapter = new LibraryAdapter(arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    //确认是否有信息
    public void confirmInfo(){
        user = userOperate.query("user");
        if (user.getLibrary().equals("null")){
            information.setVisibility(View.VISIBLE);
        }
    }
}
