package com.example.yummlyteam.app.api;

import com.example.yummlyteam.app.Util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockRecipesListInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if(Util.areMocksEnabled) {
            //Get mock file for current page
            String responseString =Util.getJsonFromAssets("SearchResponse_Hui_Page"+chain.request().url().queryParameter("start") + ".json");
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
