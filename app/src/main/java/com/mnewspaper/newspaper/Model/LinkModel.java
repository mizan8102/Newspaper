package com.mnewspaper.newspaper.Model;

public class LinkModel {
    private String link;

    public LinkModel() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkname() {
        return linkname;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public LinkModel(String link, String linkname) {
        this.link = link;
        this.linkname = linkname;
    }

    private String linkname;
}
