package com.example.android.maximfialko.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class NewsItemDTO {
    @SerializedName("subsection")
    @Nullable
    private String dto_category;

    @SerializedName("title")
    private String dto_title;

    @SerializedName("abstract")
    private String dto_abstract;

    @SerializedName("url")
    private String dto_url;

    @SerializedName("published_date")
    private String dto_published_date;

    @SerializedName("multimedia")
    @Nullable
    private ArrayList<MultimediaDTO>multimedia;

    @Nullable
    private String getSubSection() {return dto_category;}
    private String getTitile() {return dto_category;}
    private String getAbstract() {return dto_abstract;}
    private String getUrl() {return dto_url;}
    private String getPublisheDate() {return dto_published_date;}
    @Nullable
    private ArrayList<MultimediaDTO> getMultimedia() {return multimedia;}
}
