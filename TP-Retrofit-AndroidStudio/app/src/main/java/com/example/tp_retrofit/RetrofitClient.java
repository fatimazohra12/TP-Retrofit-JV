package com.example.tp_retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(boolean useXml) {
        String baseUrl = "http://10.0.2.2:8080";

        // Always recreate the Retrofit instance when switching format or baseUrl
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl);

        if (useXml) {
            builder.addConverterFactory(SimpleXmlConverterFactory.create());
        } else {
            builder.addConverterFactory(GsonConverterFactory.create());
        }

        retrofit = builder.build();

        return retrofit;
    }
}
