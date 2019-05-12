package com.example.android.maximfialko.background_update;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NewsUpdateWork extends Worker {

    public NewsUpdateWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NewsUpdateService.start(getApplicationContext());
        return Result.success();
    }
}
