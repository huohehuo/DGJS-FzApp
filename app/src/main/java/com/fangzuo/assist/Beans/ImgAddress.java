package com.fangzuo.assist.Beans;

public class ImgAddress {
    /**图片名字*/
    public String Error;
    public String picName;
    /**文件夹中图片的数量*/
    public int count;
    public ImgAddress() {
    }
    public ImgAddress(String picName, int count) {
        this.picName = picName;
        this.count = count;
        this.Error = "";
    }
    public ImgAddress(String error) {
        this.Error = error;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
