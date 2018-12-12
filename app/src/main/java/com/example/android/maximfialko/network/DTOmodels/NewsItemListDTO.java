package com.example.android.maximfialko.network.DTOmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsItemListDTO {
    @SerializedName("results")
    private List<NewsItemDTO> newsList;

    private List<NewsItemDTO> getNews() {return newsList;}
}
