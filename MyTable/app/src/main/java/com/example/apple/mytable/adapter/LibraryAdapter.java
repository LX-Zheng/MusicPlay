package com.example.apple.mytable.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.mytable.Model.Library;
import com.example.apple.mytable.R;

import java.util.ArrayList;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Library> arrayList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView floor;
        TextView detail;
        ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            floor = (TextView)view.findViewById(R.id.floor);
            detail = (TextView)view.findViewById(R.id.detail);
        }
    }

    public LibraryAdapter(ArrayList<Library> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.library_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Library library = arrayList.get(position);
        holder.floor.setText(library.getFloor());
        holder.detail.setText(library.getDetail());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
