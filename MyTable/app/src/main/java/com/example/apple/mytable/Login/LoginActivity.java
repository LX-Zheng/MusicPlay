package com.example.apple.mytable.Login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.mytable.MainActivity;
import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.OkHttp.Operate;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.MyDatabaseHepler;
import com.example.apple.mytable.SQLite.UserOperate;

import org.json.JSONArray;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Object
    private EditText account;
    private EditText password;
    private Button login_button;

    //用户账号、密码
    private String user;
    private String pwd;
    private int id;


    private MyDatabaseHepler myDatabaseHepler;
    private SQLiteDatabase db;
    private UserOperate userOperate;

    //控件初始化与事件绑定
    public void bind(){
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);
        login_button = (Button)findViewById(R.id.login_button);
        myDatabaseHepler = new MyDatabaseHepler(this,"MyTable.db",null,1);
        db = myDatabaseHepler.getWritableDatabase();
        login_button.setOnClickListener(this);
        userOperate = new UserOperate(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind();
        //自动登录
        User user = userOperate.query("user");
        String islogin = user.getIslogin();
        int remember = user.getRemember();
        id = user.getId();
        String ac = user.getAccount();
        String pa = user.getPassword();
        if("true".equals(islogin)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        //记住密码
        if (remember==1){
            account.setText(ac);
            password.setText(pa);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_button:
                user = account.getText().toString().trim();
                pwd = password.getText().toString().trim();
                RequestBody requestBody = new FormBody.Builder()
                        .add("account",user)
                        .add("password",pwd)
                        .build();
                MyHttp myHttp = new MyHttp(this);
                myHttp.post("/confirm", requestBody, new MyHttpCallback() {
                    @Override
                    public void uiRunnable(String res) {
                        if(res.equals("1")){
                            Toast.makeText(LoginActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                        } else {
                            try{
                                JSONArray jsonArray = new JSONArray(res.toString());
                                ContentValues values = new ContentValues();
                                values.put("id",jsonArray.getJSONObject(0).getInt("id"));
                                values.put("sex",jsonArray.getJSONObject(0).getInt("sex"));
                                values.put("account",jsonArray.getJSONObject(0).getString("account"));
                                values.put("name",jsonArray.getJSONObject(0).getString("name"));
                                values.put("password",jsonArray.getJSONObject(0).getString("password"));
                                values.put("state",1);
                                values.put("islogin","true");
                                values.put("library","null");
                                values.put("remember",1);
                                db.insertWithOnConflict("user",null,values,SQLiteDatabase.CONFLICT_REPLACE);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Operate operate = new Operate(LoginActivity.this);
                            operate.refresh(userOperate.query("user").getId()+"");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
