package com.example.android.maximfialko.network;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

public class MultimediaDTO {
    @SerializedName("url")
    @Nullable
    private String url;

    public String getUrl() {
        return url;
    }
}
