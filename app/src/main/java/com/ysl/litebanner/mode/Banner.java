package com.ysl.litebanner.mode;


/**
 * 轮播Banner
 * Created by YSL on 2017/4/18.
 */
public class Banner {

    public Banner() {
    }

    public Banner(String img) {
        this.img = img;
    }
    private String img;  //图片 url

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
