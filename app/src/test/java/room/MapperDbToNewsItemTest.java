package room;

import com.example.android.maximfialko.room.MapperDbToNewsItem;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MapperDbToNewsItemTest {

    @Test
    public void mapFromStringToDate() {
//        String testString = "2018-12-01T10:27:06-05:00";
        String testString = "Fri Dec 07 10:00:05 GMT+00:00 2018";
        Date expected = new Date(118, 11, 7, 10, 0,5);

        Date actual = MapperDbToNewsItem.formatDateFromDb(testString);

        assertEquals(expected, actual);
    }

}
