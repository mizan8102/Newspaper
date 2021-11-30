package com.mnewspaper.newspaper.Model;

public class BlogModel {
    public BlogModel() {
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBdes() {
        return bdes;
    }

    public void setBdes(String bdes) {
        this.bdes = bdes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BlogModel(String btitle, String bdes, String date, String imageUrl, String time, String key) {
        this.btitle = btitle;
        this.bdes = bdes;
        this.date = date;
        this.imageUrl = imageUrl;
        this.time = time;
        this.key = key;
    }

    private String btitle,bdes,date;

    private String imageUrl;


    private String time;


    private String key;

}
