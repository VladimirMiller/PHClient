package com.example.user.rhclient.api;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final int TIMEOUT = 100;
    private static final int CONNECT_TIMEOUT = 10;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    private static final String BASE_URL = "https://api.producthunt.com/v1/";

    public static ProductHuntApi createApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .build();

        return retrofit.create(ProductHuntApi.class);
    }
}
