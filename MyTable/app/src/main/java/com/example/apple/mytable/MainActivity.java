package com.example.apple.mytable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.apple.mytable.Fragment.FriendFragment;
import com.example.apple.mytable.Fragment.HomeFragment;
import com.example.apple.mytable.Fragment.MessageFragment;
import com.example.apple.mytable.Fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Object
    private TextView message;
    private TextView friend;
    private TextView home;
    private TextView mine;
    //private TextView toolbar_title;
    //private Toolbar toolbar;

    //Fragment
    private MessageFragment messageFragment;
    private FriendFragment friendFragment;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private List<Fragment> fragments = new ArrayList<>();

    //控件初始化与事件绑定
    public void bind(){
        message = (TextView)findViewById(R.id.message);
        friend = (TextView)findViewById(R.id.friend);
        home = (TextView)findViewById(R.id.home);
        mine = (TextView)findViewById(R.id.mine);
        //toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        //toolbar = (Toolbar)findViewById(R.id.toolbar);
        message.setOnClickListener(this);
        friend.setOnClickListener(this);
        home.setOnClickListener(this);
        mine.setOnClickListener(this);

        //fragments = new ArrayList<>();
        // 添加首页
        home.performClick();
        homeFragment = new HomeFragment();
        fragments.add(homeFragment);
        hideOthersFragment(homeFragment, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");
    }


    @Override
    public void onClick(View view){
        //fragmentManager = getSupportFragmentManager();
        switch (view.getId()){
            case R.id.home:
                //toolbar_title.setText("home");
                hideOthersFragment(homeFragment, false);
                break;
            case R.id.message:
                //toolbar_title.setText("message");
                if (messageFragment == null){
                    messageFragment = new MessageFragment();
                    fragments.add(messageFragment);
                    hideOthersFragment(messageFragment,true);
                } else {
                    hideOthersFragment(messageFragment,false);
                }
                break;
            case R.id.friend:
                if (friendFragment == null){
                    friendFragment = new FriendFragment();
                    fragments.add(friendFragment);
                    hideOthersFragment(friendFragment,true);
                } else {
                    hideOthersFragment(friendFragment,false);
                }
                break;
            case R.id.mine:
                //toolbar_title.setText("我的");
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    fragments.add(mineFragment);
                    hideOthersFragment(mineFragment,true);
                } else {
                    hideOthersFragment(mineFragment,false);
                }
                break;
            default:
                break;
        }
    }

    /**
     *动态显示Fragment
     * @param showFragment 要增加的fragment
     * @param add          true:增加fragment;false:切换fragment
     */
    private void hideOthersFragment(Fragment showFragment,boolean add){
        transaction = fragmentManager.beginTransaction();
        if(add) {
            transaction.add(R.id.main_container_content, showFragment);
        }
        for (Fragment fragment : fragments){
            if (showFragment.equals(fragment)){
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }
}
