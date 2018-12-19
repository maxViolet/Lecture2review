package com.example.android.maximfialko.Utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public interface Navigator {

    public abstract void openNextFragment(Fragment fragment);

    public abstract void  getContextFrom(Activity activity);
}
