package com.example.toja.worldnewscache.requests;

import android.widget.ListView;

import androidx.lifecycle.LiveData;

import com.example.toja.worldnewscache.responses.ApiResponse;
import com.example.toja.worldnewscache.responses.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("/v2/top-headlines")
    LiveData<ApiResponse<NewsResponse>> getAllNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("language") String language,
            @Query("page") String page,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey
    );

    @GET("/v2/everything")
    LiveData<ApiResponse<NewsResponse>> searchNews(
            @Query("q") String query,
            @Query("language") String language,
            @Query("page") String page,
            @Query("pageSize") String pageSize,
            @Query("apiKey") String apiKey
    );
}
