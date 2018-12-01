package com.example.android.maximfialko.room;

import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.DTOmodels.NewsItemDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapperDbToNewsItem {
    private static final String MULTIMEDIA_TYPE_IMAGE = "image";

    public static List<NewsItem> map(List<NewsItemDB> listDb) {

        final List<NewsItem> list = new ArrayList<>();

        for (NewsItemDB item_db : listDb) {
            list.add(
                    new NewsItem(
                            item_db.getTitle(),          //title
                            item_db.getImageUrl(),      //imageUrl
                            item_db.getCategory(),      //category
                            formatDateFromDb(item_db.getPublishDate()),  //publishDate
                            item_db.getPreviewText(),     //textPreview
                            item_db.getTextUrl()           //textUrl
                    )
            );
        }

        return list;

    }

    public static String formatDateFromApi(String publishedDate) {
        String result = "";
        if (publishedDate != null && publishedDate.contains("T")) {
            String[] date = publishedDate.split("T");
            String time = date[1].split("-")[0];
            String formattedTime = time.substring(0, time.lastIndexOf(":"));
            result = date[0] + " at " + formattedTime;
        }
        return result;
    }

    public static Date formatDateFromDb(String publishedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
            return dateFormat.parse(publishedDate);
        } catch (ParseException e) {
            return null;
        }
    }
}

