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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-'", Locale.ENGLISH);

    public Date transfromToDate(String dateTime) {
        try {
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        //JSON 2018-11-13T09:49:18-05:00
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
//        Date date = Date.parse(dateTime, formatter);
//
//        int year = date.getYear(); // 2010
//            int day = date.getDayOfMonth(); // 2
//            Month month = date.getMonth(); // JANUARY
//            int monthAsInt = month.getValue(); // 1
//        }
//
//        return date;
    }
}