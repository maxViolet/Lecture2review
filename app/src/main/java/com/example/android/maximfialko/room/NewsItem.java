package com.example.android.maximfialko.room;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "newsItem")
public class NewsItem {

    public NewsItem() {
    }

    @Ignore
    public NewsItem(@NonNull int id, String title, String imageUrl, String category, Date publishDate, String previewText, String textUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.textUrl = textUrl;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "publishDate")
    private Date publishDate;

    @ColumnInfo(name = "previewText")
    private String previewText;

    @ColumnInfo(name = "textUrl")
    private String textUrl;

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
        return imageUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getTextUrl() {
        return textUrl;
    }


}
