package com.example.apple.mytable.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.apple.mytable.OkHttp.Operate;
import com.example.apple.mytable.R;

import java.util.Objects;


public class FriendDetail extends AppCompatActivity implements View.OnClickListener {

    public static final String FRIEND_NAME = "friend_name";
    public static final String FRIENG_IMAGR_ID = "friend_image_id";
    public static final String FRIEND_ID = "friend_id";
    public static final String FRIEND_MEMONAME = "friend_memoname";
    public String friend_id;
    public String friendmemoname;

    private Toolbar friend_detail;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView friend_image_view;
    private LinearLayout set_name;

    public void bind(){
        friend_detail = (Toolbar)findViewById(R.id.friend_detail);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        friend_image_view = (ImageView)findViewById(R.id.friend_image_view);
        set_name = (LinearLayout)findViewById(R.id.set_name);

        set_name.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        bind();
        setSupportActionBar(friend_detail);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //获取intent传递的数据
        Intent intent = getIntent();
        String friendName = intent.getStringExtra(FRIEND_NAME);
        friendmemoname = intent.getStringExtra(FRIEND_MEMONAME);
        int ImageId = intent.getIntExtra(FRIENG_IMAGR_ID,0);
        friend_id = intent.getStringExtra(FRIEND_ID);

        collapsingToolbarLayout.setTitle(friendmemoname);
        Glide.with(this).load(R.drawable.strawberry).into(friend_image_view);
    }


    //加载自定义toolbar布局文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                Operate operate = new Operate(this);
                operate.deleteFriend(friend_id);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.set_name:
                Intent SetName = new Intent(FriendDetail.this,SetName.class);
                SetName.putExtra("id",friend_id);
                SetName.putExtra("memoname",friendmemoname);
                startActivityForResult(SetName,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = data.getExtras().getString("result");
        if(!Objects.equals(result, "null")){
            collapsingToolbarLayout.setTitle(result);
        }
    }
}
