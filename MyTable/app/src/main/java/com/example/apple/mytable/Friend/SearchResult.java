package com.example.apple.mytable.Friend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.apple.mytable.R;

public class SearchResult extends AppCompatActivity {

    public static final String USER_NAME = "user_name";
    public static final String USER_IMAGE_ID = "user_image_id";
    public static final String USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
    }
}
