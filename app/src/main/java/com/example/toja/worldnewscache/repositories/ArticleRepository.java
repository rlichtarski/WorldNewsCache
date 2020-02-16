package com.example.toja.worldnewscache.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.toja.worldnewscache.AppExecutors;
import com.example.toja.worldnewscache.persistence.ArticleDao;
import com.example.toja.worldnewscache.persistence.ArticleDatabase;
import com.example.toja.worldnewscache.requests.ServiceGenerator;
import com.example.toja.worldnewscache.responses.ApiResponse;
import com.example.toja.worldnewscache.responses.NewsResponse;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.NetworkBoundResource;
import com.example.toja.worldnewscache.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.example.toja.worldnewscache.utils.Constants.API_KEY;
import static com.example.toja.worldnewscache.utils.Constants.PAGE_SIZE;

public class ArticleRepository {

    private static final String TAG = "ArticleRepository";

    private static ArticleRepository instance;
    private ArticleDao articleDao;

    public static ArticleRepository getInstance(Context context) {
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
                if(item.getArticles() != null) {
                    Article[] articles = new Article[item.getArticles().size()];
                    List<Article> allArticlesFromDb = articleDao.getAllArticles();

                    int index = 0;
                    for(long rowId : articleDao.insertArticles((Article[]) (item.getArticles().toArray(articles)))) {
                        for(Article article : allArticlesFromDb) {
                            if (articles[index].getTitle().equals(article.getTitle())) {
                                Log.e(TAG,"saveCallResult: DUPLICATE RESULT!");
                                articleDao.deleteDuplicate(articles[index].getTitle());
                            }
                        }

                        if(rowId == -1) {
                            Log.d(TAG,"saveCallResult: CONFLICT. This article is already in cache");
                            //if the article already exists, just update it
                            articleDao.updateArticles(
                                    articles[index].getTitle(),
                                    articles[index].getAuthor(),
                                    articles[index].getDescription(),
                                    articles[index].getUrl(),
                                    articles[index].getUrlToImage(),
                                    articles[index].getPublishedAt(),
                                    articles[index].getContent()
                            );
                        }
                        index++;
                    }
                }
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
                return ServiceGenerator.getNewsApi().searchNews(query,
                        "en",
                        String.valueOf(pageNumber),
                        PAGE_SIZE,
                        API_KEY);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Article>>> searchArticlesByCategories(final String country,final String category,final int pageNumber) {
        return new NetworkBoundResource<List<Article>, NewsResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull NewsResponse item) {
                if(item.getArticles() != null) {
                    Article[] articles = new Article[item.getArticles().size()];
                    List<Article> allArticlesFromDb = articleDao.getAllArticles();

                    int index = 0;
                    for(long rowId : articleDao.insertArticles((Article[]) (item.getArticles().toArray(articles)))) {
                        for(Article article : allArticlesFromDb) {
                            if (articles[index].getTitle().equals(article.getTitle())) {
                                Log.e(TAG,"saveCallResult: DUPLICATE RESULT!");
                                articleDao.deleteDuplicate(articles[index].getTitle());
                            }
                        }

                        if(rowId == -1) {
                            Log.d(TAG,"saveCallResult: CONFLICT. This article is already in cache");
                            //if the article already exists, just update it
                            articleDao.updateArticles(
                                    articles[index].getTitle(),
                                    articles[index].getAuthor(),
                                    articles[index].getDescription(),
                                    articles[index].getUrl(),
                                    articles[index].getUrlToImage(),
                                    articles[index].getPublishedAt(),
                                    articles[index].getContent()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Article> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Article>> loadFromDb() {
                return articleDao.searchArticles(category, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewsResponse>> createCall() {
                return ServiceGenerator.getNewsApi().getAllNews(
                        country,
                        category,
                        "en",
                        String.valueOf(pageNumber),
                        PAGE_SIZE,
                        API_KEY
                );
            }
        }.getAsLiveData();
    }
}
