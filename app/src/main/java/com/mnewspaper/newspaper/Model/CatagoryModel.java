package com.mnewspaper.newspaper.Model;

public class CatagoryModel {
    private String paperName;

    public String getPaperImage() {
        return paperImage;
    }

    public void setPaperImage(String paperImage) {
        this.paperImage = paperImage;
    }

    private String paperImage;

    public CatagoryModel() {
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPaperLink() {
        return paperLink;
    }

    public void setPaperLink(String paperLink) {
        this.paperLink = paperLink;
    }

    private String shortDes;
    private String ratings;

    public CatagoryModel(String paperImage,String paperName, String shortDes, String ratings, String paperLink) {
        this.paperName = paperName;
        this.shortDes = shortDes;
        this.ratings = ratings;
        this.paperLink = paperLink;
        this.paperImage=paperImage;
    }

    private String paperLink;
}
