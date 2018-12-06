package com.example.android.maximfialko.room;

import android.util.Log;

import com.example.android.maximfialko.data.NewsItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapperDbToNewsItem {
    public static List<NewsItem> map(List<NewsItemDB> listDb) {

        final List<NewsItem> list = new ArrayList<>();

        for (NewsItemDB item_db : listDb) {
            list.add(
                    new NewsItem(
                            item_db.getId(),                             //id - autogenerated by Room
                            item_db.getTitle(),                          //title
                            item_db.getImageUrl(),                       //imageUrl
                            item_db.getCategory(),                       //category
                            formatDateFromDb(item_db.getPublishDate()),  //publishDate
                            item_db.getPreviewText(),                    //textPreview
                            item_db.getTextUrl()                         //textUrl
                    )
            );
//            Log.d("room","CONVERTED DB -> NewsItems");
//            Log.d("room","items converted, " + item_db.getCategory() + ", " +
//                    item_db.getPublishDate() + ", " + item_db.getTitle());
        }

        return list;

    }

    public static Date formatDateFromDb(String publishedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
//            Log.d("room", dateFormat.parse(publishedDate).toString());
            return dateFormat.parse(publishedDate);
        } catch (ParseException e) {
//            Log.d("room", "formatDateFromDb returned NULL");
            return new Date(11, 11, 11);
        }
    }
}

