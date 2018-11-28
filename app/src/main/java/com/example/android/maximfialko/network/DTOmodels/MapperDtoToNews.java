package com.example.android.maximfialko.network.DTOmodels;

import com.example.android.maximfialko.data.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MapperDtoToNews {
    private static final String MULTIMEDIA_TYPE_IMAGE = "image";

    public static List<NewsItem> map(List<NewsItemDTO> listDto) {

        final List<NewsItem> list = new ArrayList<>();

        for (NewsItemDTO item_dto : listDto) {
            list.add(
                    new NewsItem(
                            item_dto.getTitle(),        //title
                            mapImage(item_dto.getMultimedia()), //imageUrl
                            item_dto.getSection(),      //category
                            item_dto.getPublishDate(),  //publishDate
                            item_dto.getAbstract(),     //textPreview
                            item_dto.getUrl()           //textUrl
                    )
            );
        }

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
