package com.example.android.maximfialko.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.maximfialko.application.MyApplication;

import io.reactivex.Single;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class NetworkCheckUtils {

    public static NetworkCheckUtils sNetworkUtils = new NetworkCheckUtils();
    private NetworkReceiver mNetworkReceiver = new NetworkReceiver();
    private Subject<Boolean> mNetworkState = BehaviorSubject.createDefault(isNetworkAvailable());

    public NetworkReceiver getReceiver() {
        return mNetworkReceiver;
    }

    public Single<Boolean> getOnlineNetwork() {
        return mNetworkState
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean online) throws Exception {
                        return online;
                    }
                })
                .firstOrError();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mNetworkState.onNext(isNetworkAvailable());
        }

    }

}