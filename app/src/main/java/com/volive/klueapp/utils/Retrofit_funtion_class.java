package com.volive.klueapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by volive on 2/5/2018.
 */

public class Retrofit_funtion_class {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder().baseUrl(DZ_URL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
