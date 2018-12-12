package com.example.android.maximfialko.network;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Endpoint {
    @NonNull
    @GET("svc/topstories/v2/{category}.json")
    Single<TopStoriesResponse> getNews (@Path("category") String category);
}
