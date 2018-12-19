package com.example.android.maximfialko.network.DTOmodels;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public class NewsItemDTO {
    @SerializedName("section")
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
    private ArrayList<MultimediaDTO> multimedia;

    @Nullable
    public String getSection() {
        return dto_category;
    }

    public String getTitle() {
        return dto_title;
    }

    public String getAbstract() {
        return dto_abstract;
    }

    public String getUrl() {
        return dto_url;
    }

    public String getPublishDate() {
//        Log.d("room", "DTO ITEMS original date: " + dto_published_date);
        return dto_published_date;
    }

    @Nullable
    public ArrayList<MultimediaDTO> getMultimedia() {
        return multimedia;
    }
}
