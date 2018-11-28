package com.example.android.maximfialko.data;

import java.util.Date;

//--------------------------------------------------------------------------------------------------
public class NewsItem {

    private final String title;
    private final String imageUrl;
    private final Category category;
    private final Date publishDate;
    private final String previewText;
    private final String fullText;
    private final int itemId;

    //----------------------------------------------------------------------------------------------
    public NewsItem(String title, String imageUrl, Category category, Date publishDate, String previewText, String fullText, int itemId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.fullText = fullText;
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getItemId() {return itemId; }

    public Category getCategory() {
        return category;
    }

    public Date getPublishDate() {
        //Date publishDate = Date.current;
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getFullText() {
        return fullText;
    }
}
//--------------------------------------------------------------------------------------------------


