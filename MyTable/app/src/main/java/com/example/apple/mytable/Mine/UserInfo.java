package com.example.apple.mytable.Mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.UserOperate;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {

    //UI Object
    private Toolbar toolbar;
    private TextView user_sex;
    private TextView user_name;
    private LinearLayout user_info_sex;

    private String[] sexArry = new String[]{"男","女"};
    private int isex;
    private int sex;
    private int userid;
    private String name;
    private UserOperate userOperate;
    private User user;


    //事件绑定
    public void bind(){
        toolbar = (Toolbar)findViewById(R.id.change_user_info);
        user_sex = (TextView)findViewById(R.id.user_sex);
        user_name = (TextView)findViewById(R.id.user_name);
        user_info_sex = (LinearLayout)findViewById(R.id.user_info_sex);
        user_info_sex.setOnClickListener(this);
        userOperate = new UserOperate(this);
        user = userOperate.query("user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        bind();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sex = user.getSex();
        userid = user.getId();
        name = user.getName();

        if(sex==0){
            user_sex.setText("男");
        } else if (sex ==1){
            user_sex.setText("女");
        } else {
            user_sex.setText("不详");
        }
        user_name.setText(name);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            //修改性别
            case R.id.user_info_sex:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setSingleChoiceItems(sexArry, sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isex = i;
                        dialogInterface.dismiss();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("id",userid+"")
                                .add("sex",i+"")
                                .build();
                        MyHttp myHttp = new MyHttp(UserInfo.this);
                        myHttp.post("/changeSex", requestBody, new MyHttpCallback() {
                            @Override
                            public void uiRunnable(String res) {
                                if(res.equals("1")){
                                    user_sex.setText(sexArry[isex]);
                                    userOperate.update("user","sex",isex,userid+"");
                                    Toast.makeText(UserInfo.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UserInfo.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
