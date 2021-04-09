package com.example.yummlyteam.app.api;

import com.example.yummlyteam.app.Util;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if(Util.areMocksEnabled) {
            String responseString =Util.getJsonFromAssets("SearchResponse_Hui.json");
            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        } else {
            response = chain.proceed(chain.request());
        }
        return response;
    }
}
