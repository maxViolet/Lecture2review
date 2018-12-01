package room;

import com.example.android.maximfialko.room.MapperDbToNewsItem;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MapperDbToNewsItemTest {

    @Test
    public void mapFromStringToDate() {
        String testString = "2018-12-01T10:27:06-05:00";
        Date expected = new Date(118, 11, 1, 10, 27,6);

        Date actual = MapperDbToNewsItem.formatDateFromDb(testString);

        assertEquals(expected, actual);
    }

}
