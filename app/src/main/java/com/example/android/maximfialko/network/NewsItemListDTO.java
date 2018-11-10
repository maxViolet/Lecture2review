package com.example.android.maximfialko.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsItemListDTO {
    @SerializedName("results")
    private List<NewsItemListDTO> newsList;

    private List<NewsItemListDTO> getNews() {return newsList;}
}
