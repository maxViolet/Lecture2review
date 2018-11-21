package com.example.android.maximfialko.Utils;

import android.view.View;

import androidx.annotation.Nullable;

public class Visibility {
    public static void setVisible(@Nullable View view, boolean show) {
        if (view == null) return;

        int visibility = show ? View.VISIBLE : View.GONE;
        view.setVisibility(visibility);
    }
}
