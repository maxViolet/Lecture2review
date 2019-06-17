package com.example.android.maximfialko.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "newsItemDB")
public class NewsItemDB {

    public NewsItemDB(@NonNull String title, String imageUrl, String category, String publishDate, String previewText, String textUrl) {
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
    private String publishDate;
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
        return category;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }
}
