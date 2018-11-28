package com.example.android.maximfialko.network.DTOmodels;

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
    private Date dto_published_date;

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

    public Date getPublishDate() {
        return dto_published_date;
    }

    @Nullable
    public ArrayList<MultimediaDTO> getMultimedia() {
        return multimedia;
    }

    /*public String checkAndReturnImageUrl() {
        if (getMultimedia().size() != 0) {
            for (MultimediaDTO multimedia : getMultimedia()) {
                if (multimedia.getFormat().equals("thumbLarge")) {
                    return multimedia.getUrl();
                }
            }
        }
        return "";
    }*/

    //TODO gson date converter // look: https://stackoverflow.com/questions/23095793/custom-gson-deserializer-for-date-never-gets-called?rq=1
//    Gson gsonDate = new GsonBuilder()
//            .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

}
