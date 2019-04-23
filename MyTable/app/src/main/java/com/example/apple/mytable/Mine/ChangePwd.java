package com.example.apple.mytable.Mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.UserOperate;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ChangePwd extends AppCompatActivity {

    //UI Object
    private Toolbar change;
    private TextView change_tittle;
    private EditText original_password;
    private EditText new_password;
    private EditText confirm_password;
    private Button finish;
    private UserOperate userOperate;
    private User user;

    //控件初始化与事件绑定
    public void bind(){
        change = (Toolbar)findViewById(R.id.change);
        change_tittle = (TextView)findViewById(R.id.change_title);
        original_password = (EditText)findViewById(R.id.original_password);
        new_password = (EditText)findViewById(R.id.new_password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        finish = (Button)findViewById(R.id.finish);
        userOperate = new UserOperate(this);
        user = userOperate.query("user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        bind();
        setSupportActionBar(change);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm_password.getText().toString().equals(new_password.getText().toString())){
                    String password = new_password.getText().toString().trim();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("account",user.getAccount())
                            .add("password",password)
                            .build();
                    MyHttp myHttp = new MyHttp(ChangePwd.this);
                    myHttp.post("/changePwd", requestBody, new MyHttpCallback() {
                        @Override
                        public void uiRunnable(String res) {
                            Toast.makeText(ChangePwd.this,res,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePwd.this,"密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
