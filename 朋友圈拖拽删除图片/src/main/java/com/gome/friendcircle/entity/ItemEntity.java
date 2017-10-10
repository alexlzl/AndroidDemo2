package com.gome.friendcircle.entity;

import java.io.Serializable;


public class ItemEntity implements Serializable{
    private int id;
    private int img;

    public ItemEntity(int id, int img) {
        this.id = id;
        this.img = img;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
