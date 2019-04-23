package com.example.apple.mytable.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.mytable.Model.History;
import com.example.apple.mytable.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<History> arrayList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout history;
        TextView history_name;
        ImageView history_delete;

        ViewHolder(View view){
            super(view);
            history = (LinearLayout)view.findViewById(R.id.history);
            history_name = (TextView)view.findViewById(R.id.history_name);
            history_delete = (ImageView)view.findViewById(R.id.history_delete);
        }
    }

    public HistoryAdapter(ArrayList<History> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.record_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.history_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Log.d("onClick: ",position+"");
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = arrayList.get(position);
        Log.d( "onBindViewHolder: ",history.getName());
        holder.history_name.setText(history.getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
