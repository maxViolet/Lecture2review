package com.example.android.maximfialko.utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public interface Navigator {
    //TODO put it to the use
    public abstract void openNextFragment(Fragment fragment);

    public abstract void  getContextFrom(Activity activity);
}
