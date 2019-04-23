package com.example.apple.mytable.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.mytable.Friend.SearchResult;
import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;
    private List<Friend> userList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout user;
        ImageView userImage;
        TextView userName;

        public ViewHolder(View view){
            super(view);
            user = (LinearLayout)view.findViewById(R.id.user);
            userImage = (ImageView)view.findViewById(R.id.user_image);
            userName = (TextView)view.findViewById(R.id.user_name);
        }
    }

    public SearchAdapter(List<Friend> users){
        userList = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int position  = viewHolder.getAdapterPosition();
                Friend user = userList.get(position);
                Intent intent = new Intent(mContext,SearchResult.class);
                intent.putExtra(SearchResult.USER_NAME,user.getName());
                intent.putExtra(SearchResult.USER_IMAGE_ID,user.getImageId());
                intent.putExtra(SearchResult.USER_ID,user.getId());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        Friend user = userList.get(position);
        viewHolder.userName.setText(user.getName());
        viewHolder.userImage.setImageResource(user.getImageId());
    }

    @Override
    public int getItemCount(){
        return userList.size();
    }

}
