package com.example.android.maximfialko.network.DTOmodels;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

public class MultimediaDTO {
    @SerializedName("type")
    private String type;

    @SerializedName("format")
    private String format;

    @SerializedName("url")
    @Nullable
    private String url;

    public String getUrl() {
        return url;
    }

    public String getFormat() { return format; }

    public String getType() { return type; }

}
