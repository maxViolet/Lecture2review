package com.example.android.maximfialko.room;

import android.util.Log;

import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.DTOmodels.MultimediaDTO;
import com.example.android.maximfialko.network.DTOmodels.NewsItemDTO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MapperDtoToDb {
    private static final String MULTIMEDIA_TYPE_IMAGE = "image";

    public static List<NewsItemDB> map(List<NewsItemDTO> listDto) {

        final List<NewsItemDB> list = new ArrayList<>();
        Log.d("room","items converted, START");

        for (NewsItemDTO item_dto : listDto) {

            list.add(
                    new NewsItemDB(
                            item_dto.getTitle(),                //title
                            mapImage(item_dto.getMultimedia()), //imageUrl
                            item_dto.getSection(),              //category
                            item_dto.getPublishDate().toString(),  //publishDate
                            item_dto.getAbstract(),             //textPreview
                            item_dto.getUrl()                   //textUrl
                    )
            );
            Log.d("room","items converted, " + item_dto.getSection() + ", " +
                    item_dto.getPublishDate().toString() + ", " + item_dto.getTitle());
        }

        Log.d("room","items converted, END");
        return list;
    }

    @Nullable
    private static String mapImage(@Nullable List<MultimediaDTO> multimedias) {

        if (multimedias == null || multimedias.isEmpty()) {
            return null;
        }

        final int imageInmMaximumQualityIndex = multimedias.size() - 1;
        final MultimediaDTO multimedia = multimedias.get(imageInmMaximumQualityIndex);

        if (!multimedia.getType().equals(MULTIMEDIA_TYPE_IMAGE)) {
            return null;
        }

        return multimedia.getUrl();
    }

}
