package com.trng.znews.Models;

public class News {
    private int id;

    private String mTitle;

    private String mSection;

    private String mAuthor;

    private String mDate;

    private String mUrl;

    private String mThumbnail;

    public News(int id, String title, String section, String author, String date, String url, String thumbnail) {
        id =  id;
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
        mThumbnail = thumbnail;
    }

    public News( String title, String author, String section, String date, String url, String thumbnail) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
        mThumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
