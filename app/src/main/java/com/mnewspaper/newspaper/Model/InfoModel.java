package com.mnewspaper.newspaper.Model;

public class InfoModel {
    public InfoModel() {
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

    private String bookname;

    public InfoModel(String bookname, String pdf_link) {
        this.bookname = bookname;
        this.pdf_link = pdf_link;
    }

    private String pdf_link;


}
