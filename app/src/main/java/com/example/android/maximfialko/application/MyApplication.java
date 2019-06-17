package com.example.android.maximfialko.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.android.maximfialko.background_update.NewsUpdateWork;
import com.example.android.maximfialko.utils.NetworkCheckUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MyApplication extends Application {
    private static MyApplication sMyApplication;
    private static int updatePeriodInMinutes = 180;

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;

        registerReceiver(NetworkCheckUtils.sNetworkUtils.getReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(NewsUpdateWork.class, updatePeriodInMinutes, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(workRequest);
    }

    public static Context getContext() {
        return sMyApplication;
    }
}
