package com.code.recyclerviewpager;

import java.io.Serializable;

/**
 * Created by shenhao on 2018/11/22.
 */

public class DataBean implements Serializable{

    private String title;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
