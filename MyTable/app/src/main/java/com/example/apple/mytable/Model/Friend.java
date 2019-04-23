package com.example.apple.mytable.Model;

public class Friend {
    private int imageId;
    private String name;
    private String id;
    private String letters;
    private String memoname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getMemoname() {
        return memoname;
    }

    public void setMemoname(String memoname) {
        this.memoname = memoname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Friend(int imageId, String name, String id, String letters,String memoname) {
        this.imageId = imageId;
        this.name = name;
        this.id = id;
        this.letters = letters;
        this.memoname = memoname;
    }

    public Friend(int imageId, String name,String id){
        this.imageId = imageId;
        this.name = name;
        this.id = id;
    }

    public Friend(){}
}
