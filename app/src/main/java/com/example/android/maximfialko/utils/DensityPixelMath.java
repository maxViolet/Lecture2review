package com.example.android.maximfialko.utils;

import android.content.Context;

public class DensityPixelMath {
    private Context dpContext;

    public DensityPixelMath(Context context) {
        dpContext = context;
    }

    //методы с контекстом
    public float dpFromPx(final int px) {
        return (int) (px / dpContext.getResources().getDisplayMetrics().density);
    }

    public float pxFromDp(final float dp) {
        return (int) (dpContext.getResources().getDisplayMetrics().density);
    }

    //static методы без контекста
//    public static int dpToPx(int dp) {
//        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
//    }
//
//    public static int pxToDp(int px) {
//        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
//    }
}
