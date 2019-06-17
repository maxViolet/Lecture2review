package com.example.android.maximfialko.network;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RestApi {

    private final String API_KEY = "qmrfMfNl5hRhJGiMXaSXLGe25jPGIn0T";
    private final String URL = "https://api.nytimes.com";
    private final int TIMEOUT_IN_SECONDS = 2;

    private final Endpoint topStoriesEndpoint;
    private static RestApi sRestApi;

    public static synchronized RestApi getInstance() {
        if (sRestApi == null) {
            sRestApi = new RestApi();
        }
        return sRestApi;
    }

    private RestApi() {
        final OkHttpClient httpClient = buildOkHttpClient();
        final Retrofit retrofit = buildRetrofitClient(httpClient);

        //init endpoints here. It's can be more then one topStoriesEndpoint
        topStoriesEndpoint = retrofit.create(Endpoint.class);
    }

    @NonNull
    private Retrofit buildRetrofitClient(@NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @NonNull
    private OkHttpClient buildOkHttpClient() {
        final HttpLoggingInterceptor networkLogInterceptor = new HttpLoggingInterceptor();
        networkLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(ApiKeyInterceptor.create(API_KEY))
                .addInterceptor(networkLogInterceptor)
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public Endpoint topStoriesEndpoint() {
        return topStoriesEndpoint;
    }
}
