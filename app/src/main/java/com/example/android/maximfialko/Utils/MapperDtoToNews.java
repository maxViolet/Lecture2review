package com.example.android.maximfialko.Utils;

import com.example.android.maximfialko.data.Category;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.DTOmodels.MultimediaDTO;
import com.example.android.maximfialko.network.DTOmodels.NewsItemDTO;

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
                            item_dto.getTitle(),
                            mapImage(item_dto.getMultimedia()),
                            item_dto.getSection(),
                            item_dto.getPublishDate(),
                            item_dto.getAbstract(),
                            item_dto.getUrl()
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

    /*static String checkAndReturnImageUrl() {
        if (getMultimedia().size() != 0) {
            for (MultimediaDTO multimedia : getMultimedia()) {
                if (multimedia.getFormat().equals("thumbLarge")) {
                    return multimedia.getUrl();
                }
            }
        }
        return "";
    }*/

}
