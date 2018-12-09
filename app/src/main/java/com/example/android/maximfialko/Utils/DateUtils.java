package com.example.android.maximfialko.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;

public class DateUtils {
    public static CharSequence formatDateTime(Context context, Date dateTime) {
        return android.text.format.DateUtils.getRelativeDateTimeString(
                context,
                dateTime.getTime(),
                HOUR_IN_MILLIS,
                5 * DAY_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE
        );
    }

    public static Date formatDateFromDb(String publishedDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.ENGLISH);
        try {
            return dateFormat.parse(publishedDate);
        } catch (ParseException e) {
            return new Date(11, 11, 11);
        }
    }

}