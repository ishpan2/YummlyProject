package com.example.yummlyteam.app.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String BASE_URL = "https://api.yummly.com/v1/api/";

    public static Retrofit getClient(Interceptor mocksInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient
                .Builder();
        if(mocksInterceptor!=null) {
            clientBuilder.addInterceptor(mocksInterceptor);
        }
        OkHttpClient client = clientBuilder.build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}



