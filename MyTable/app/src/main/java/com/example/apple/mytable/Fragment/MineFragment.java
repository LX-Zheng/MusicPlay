package com.example.apple.mytable.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mytable.Mine.ChangePwd;
import com.example.apple.mytable.Mine.Footprint;
import com.example.apple.mytable.Mine.Setting;
import com.example.apple.mytable.Mine.UserInfo;
import com.example.apple.mytable.Model.User;
import com.example.apple.mytable.OkHttp.MyHttp;
import com.example.apple.mytable.OkHttp.MyHttpCallback;
import com.example.apple.mytable.R;
import com.example.apple.mytable.SQLite.UserOperate;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class MineFragment extends Fragment implements View.OnClickListener {

    //UI Object
    private LinearLayout user_content;
    private LinearLayout changePwd;
    private LinearLayout change_phone;
    private LinearLayout account_set;
    private TextView user_name;
    private TextView user_account;
    private ImageView my_state_image;
    private TextView my_state_text;
    private TextView change_state;
    private TextView footprint;

    private User user;
    private UserOperate userOperate;
    private String[] stateArry = new String[]{"隐身","在线"};
    private int userid;

    private static final int STATE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        //控件初始化
        user_content = (LinearLayout)view.findViewById(R.id.user_content);
        changePwd = (LinearLayout) view.findViewById(R.id.changePwd);
        change_phone = (LinearLayout) view.findViewById(R.id.phone_change);
        account_set = (LinearLayout)view.findViewById(R.id.account_set);
        user_name = (TextView)view.findViewById(R.id.user_name);
        user_account = (TextView)view.findViewById(R.id.user_account);
        my_state_image = (ImageView)view.findViewById(R.id.my_state_image);
        my_state_text = (TextView)view.findViewById(R.id.my_state_text);
        change_state = (TextView)view.findViewById(R.id.change_state);
        footprint = (TextView)view.findViewById(R.id.footprint);

        userOperate = new UserOperate(getActivity());
        user = userOperate.query("user");
        userid = user.getId();
        user_name.setText(user.getName());
        user_account.setText(user.getAccount());


        //事件绑定
        user_content.setOnClickListener(this);
        changePwd.setOnClickListener(this);
        change_phone.setOnClickListener(this);
        change_state.setOnClickListener(this);
        account_set.setOnClickListener(this);
        footprint.setOnClickListener(this);

        //改变用户的状态
        Message message = new Message();
        message.what = STATE;
        handler.sendMessage(message);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case  R.id.change_state:
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setSingleChoiceItems(stateArry, user.getState(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int position = i;
                        dialogInterface.dismiss();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("id",userid+"")
                                .add("state",i+"")
                                .build();
                        MyHttp myHttp = new MyHttp(getActivity());
                        myHttp.post("/changeState", requestBody, new MyHttpCallback() {
                            @Override
                            public void uiRunnable(String res) {
                                if(res.equals("1")){
                                    userOperate.update("user","state",position,userid+"");
                                    Message message = new Message();
                                    message.what = STATE;
                                    handler.sendMessage(message);
                                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.show();
                break;
            case R.id.footprint:
                Intent footprintIntent = new Intent(getActivity(),Footprint.class);
                startActivity(footprintIntent);
                break;
            case R.id.user_content:
                Intent intent = new Intent(getActivity(),UserInfo.class);
                startActivity(intent);
                break;
            case R.id.changePwd:
                Intent changePwd = new Intent(getActivity(),ChangePwd.class);
                startActivity(changePwd);
                break;
            case R.id.account_set:
                Intent Setting = new Intent(getActivity(),Setting.class);
                startActivity(Setting);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case STATE:
                    user = userOperate.query("user");
                    if(user.getState() == 1){
                        my_state_text.setText("在线");
                        my_state_image.setImageResource(R.drawable.ic_green_point);
                    } else {
                        my_state_text.setText("隐身");
                        my_state_image.setImageResource(R.drawable.ic_yellow_point);
                    }
            }
        }
    };
}
