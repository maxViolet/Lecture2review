package com.example.android.maximfialko.utils;

import android.os.Build;

public class VersionControl {
    //Oreo = 26 API
    public static boolean atLeastOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
