package com.example.apple.mytable.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Friend> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout friend;
        ImageView friendImage;
        TextView friendName;

        ViewHolder(View view){
            super(view);
            friend = (LinearLayout)view.findViewById(R.id.friend);
            friendImage = (ImageView)view.findViewById(R.id.friend_image);
            friendName = (TextView)view.findViewById(R.id.friend_name);
        }
    }

    public HomeAdapter(ArrayList<Friend> arrayList){
        this.list = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = viewHolder.getAdapterPosition();
//                //Friend friend = new Friend();
//            }
//        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        Friend friend = list.get(position);
        viewHolder.friendImage.setImageResource(friend.getImageId());
        viewHolder.friendName.setText(friend.getName());
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}
