package com.example.toja.worldnewscache.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.toja.worldnewscache.AppExecutors;
import com.example.toja.worldnewscache.persistence.ArticleDao;
import com.example.toja.worldnewscache.persistence.ArticleDatabase;
import com.example.toja.worldnewscache.responses.ApiResponse;
import com.example.toja.worldnewscache.responses.NewsResponse;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.NetworkBoundResource;
import com.example.toja.worldnewscache.utils.Resource;

import java.util.List;

public class ArticleRepository {

    private ArticleRepository instance;
    private ArticleDao articleDao;

    public ArticleRepository getInstance(Context context) {
        if(instance == null) {
            instance = new ArticleRepository(context);
        }
        return instance;
    }

    private ArticleRepository(Context context) {
        articleDao = ArticleDatabase.getInstance(context).getArticleDao();
    }

    public LiveData<Resource<List<Article>>> searchArticlesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Article>, NewsResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull NewsResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Article> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Article>> loadFromDb() {
                return articleDao.searchArticles(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewsResponse>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }
}
