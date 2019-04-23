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

import com.example.apple.mytable.Friend.FriendDetail;
import com.example.apple.mytable.Model.Friend;
import com.example.apple.mytable.R;

import java.util.List;


public class FriendAdpater extends RecyclerView.Adapter<FriendAdpater.ViewHolder> {

    private Context mContext;
    private List<Friend> friends;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout friend;
        ImageView friendImage;
        TextView friendName;
        TextView tag;

        public ViewHolder(View view){
            super(view);
            friend = (LinearLayout)view.findViewById(R.id.friend);
            friendImage = (ImageView)view.findViewById(R.id.friend_image);
            friendName = (TextView)view.findViewById(R.id.friend_name);
            tag = (TextView)view.findViewById(R.id.tag);
        }
    }

    public FriendAdpater(List<Friend> friendList){
        friends = friendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position  = viewHolder.getAdapterPosition();
                Friend friend = friends.get(position);
                Intent intent = new Intent(mContext,FriendDetail.class);
                intent.putExtra(FriendDetail.FRIEND_NAME,friend.getName());
                intent.putExtra(FriendDetail.FRIENG_IMAGR_ID,friend.getImageId());
                intent.putExtra(FriendDetail.FRIEND_ID,friend.getId());
                intent.putExtra(FriendDetail.FRIEND_MEMONAME,friend.getMemoname());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        int section = getSectionForPosition(position);
        Friend friend = friends.get(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setText(friends.get(position).getLetters());
        } else {
            viewHolder.tag.setVisibility(View.GONE);
        }

        viewHolder.friendName.setText(friend.getMemoname());
        viewHolder.friendImage.setImageResource(friend.getImageId());
    }

    @Override
    public int getItemCount(){
        return friends.size();
    }


    /**
     *根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position){
        return friends.get(position).getLetters().charAt(0);
    }

    /**
     *根据分类的首字母的Char ascii值获取其第一次出现的位置
     */
    public int getPositionForSection(int section){
        for(int i=0;i<getItemCount();i++){
            String sortStr = friends.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if(firstChar == section){
                return i;
            }
        }
        return -1;
    }
}
