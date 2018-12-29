package com.example.android.maximfialko.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.android.maximfialko.utils.NetworkUtils;

public class MyApplication extends Application {

    private static MyApplication sMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        // TODO:  your logic
        registerReceiver(NetworkUtils.sNetworkUtils.getReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static Context getContext() {
        return sMyApplication;
    }

}
