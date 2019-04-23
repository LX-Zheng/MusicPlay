package com.example.apple.mytable.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.TableOperate;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SetName extends AppCompatActivity {

    private Toolbar set_name;
    private EditText set_content;
    private String friend_id;
    private boolean ischanged;

    public void bind(){
        set_name = (Toolbar)findViewById(R.id.set_name);
        setSupportActionBar(set_name);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        set_content = (EditText)findViewById(R.id.set_content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        bind();
        Intent intent = getIntent();
        friend_id = intent.getStringExtra("id");
        String memoname = intent.getStringExtra("memoname");
        set_content.setText(memoname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.set_name_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                if(ischanged){
                    intent.putExtra("result",set_content.getText().toString().trim());
                }else {
                    intent.putExtra("result","null");
                }
                SetName.this.setResult(1,intent);
                finish();
                return true;
            case R.id.set_finish:
                final String memo_name = set_content.getText().toString();
                RequestBody requestBody = new FormBody.Builder()
                        .add("memoname",memo_name)
                        .add("id",friend_id)
                        .build();
                MyHttp myHttp = new MyHttp(SetName.this);
                myHttp.post("/setname", requestBody, new MyHttpCallback() {
                    @Override
                    public void uiRunnable(String res) {
                        if(res.equals("1")){
                            ischanged = true;
                            TableOperate tableOperate = new TableOperate(SetName.this);
                            tableOperate.update("friend",memo_name,friend_id);
                            Toast.makeText(SetName.this,"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            if(ischanged){
                intent.putExtra("result",set_content.getText().toString().trim());
            }else {
                intent.putExtra("result","null");
            }
            SetName.this.setResult(1,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
