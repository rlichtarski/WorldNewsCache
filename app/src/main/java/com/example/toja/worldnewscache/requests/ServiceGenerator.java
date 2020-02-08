package com.example.toja.worldnewscache.requests;

import com.example.toja.worldnewscache.utils.Constants;
import com.example.toja.worldnewscache.utils.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(Constants.NEWS_BASE_URL)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static NewsApi newsApi = retrofit.create(NewsApi.class);

    public static NewsApi getNewsApi() {
        return newsApi;
    }
}
