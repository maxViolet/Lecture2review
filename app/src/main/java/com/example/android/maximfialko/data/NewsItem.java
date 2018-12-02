package com.example.android.maximfialko.data;

import java.util.Date;

public class NewsItem {

    private final int id;
    private final String title;
    private final String imageUrl;
    private final String category;
    private final Date publishDate;
    private final String previewText;
    private final String textUrl;

    public NewsItem(int id, String title, String imageUrl, String category, Date publishDate, String previewText, String textUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.textUrl = textUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

}


