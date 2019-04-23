package com.example.apple.mytable.adapter;

import com.example.apple.mytable.Model.Friend;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Friend> {
    public int compare(Friend a,Friend b){
        if (a.getLetters().equals("@") || b.getLetters().equals("#")) {
            return -1;
        } else if(a.getLetters().equals("#")||b.getLetters().equals("@")){
            return 1;
        } else {
            return a.getLetters().compareTo(b.getLetters());
        }
    }
}
