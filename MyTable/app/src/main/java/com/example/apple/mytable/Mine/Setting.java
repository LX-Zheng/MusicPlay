package com.example.apple.mytable.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.apple.mytable.Login.LoginActivity;
import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.UserOperate;

import java.util.Objects;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    private Toolbar account_set;
    private LinearLayout exit;
    private LinearLayout logout;
    private UserOperate userOperate;
    private User user;

    private void bind(){
        account_set = (Toolbar)findViewById(R.id.account_set);
        exit = (LinearLayout)findViewById(R.id.exit);
        logout = (LinearLayout)findViewById(R.id.logout);
        userOperate = new UserOperate(this);

        exit.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bind();
        setSupportActionBar(account_set);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.exit:
                user = userOperate.query("user");
                userOperate.updateState("user",user.getId()+"");
                Intent login = new Intent(Setting.this,LoginActivity.class);
                startActivity(login);
                finish();
                break;
            case R.id.logout:
                userOperate.delete();
                Intent logout = new Intent(Setting.this,LoginActivity.class);
                startActivity(logout);
                finish();
                break;
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
