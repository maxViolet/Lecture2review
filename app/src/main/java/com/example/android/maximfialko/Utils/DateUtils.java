package com.example.android.maximfialko.Utils;

import android.content.Context;

import java.util.Date;

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
}