package com.example.toja.worldnewscache.requests;

import com.example.toja.worldnewscache.utils.Constants;
import com.example.toja.worldnewscache.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.toja.worldnewscache.utils.Constants.CONNECTION_TIMEOUT;
import static com.example.toja.worldnewscache.utils.Constants.READ_TIMEOUT;
import static com.example.toja.worldnewscache.utils.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(Constants.NEWS_BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static NewsApi newsApi = retrofit.create(NewsApi.class);

    public static NewsApi getNewsApi() {
        return newsApi;
    }
}
