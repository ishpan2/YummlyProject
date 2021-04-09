package com.example.yummlyteam.app.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String BASE_URL = "https://api.yummly.com/v1/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit==null) {
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .addInterceptor(new MockInterceptor())
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}



