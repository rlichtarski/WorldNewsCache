package com.example.toja.worldnewscache.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.toja.worldnewscache.repositories.ArticleRepository;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.Resource;

import java.util.List;

public class ArticleListViewModel extends AndroidViewModel {

    private static final String TAG = "ArticleListViewModel";

    public enum ViewState {CATEGORIES, ARTICLES}
    public enum SearchType {QUERY, CATEGORY}

    private MutableLiveData<SearchType> searchType;
    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Article>>> articles = new MediatorLiveData<>();

    private ArticleRepository articleRepository;

    private boolean mIsPerformingQuery;
    private boolean mIsQueryExhausted;
    private int pageNumber;
    private String query, category, country;
    private boolean cancelRequest;
    private long requestStartTime;

    public static final String QUERY_EXHAUSTED = "No more results";
    public static final String NO_RESULTS = "No results found";

    public ArticleListViewModel(@NonNull Application application) {
        super(application);
        articleRepository = ArticleRepository.getInstance(application);
        mIsPerformingQuery = false;
        init();
    }

    private void init() {
        if(viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
        if(searchType == null) {
            searchType = new MutableLiveData<>();
        }
    }

    public LiveData<Resource<List<Article>>> getArticles() {
        return articles;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setViewCategories() {
        viewState.setValue(ViewState.CATEGORIES);
    }

    public void setSearchQueryType() {
        searchType.setValue(SearchType.QUERY);
    }

    public void setSearchCategoryType() {
        searchType.setValue(SearchType.CATEGORY);
    }

    public void searchArticlesApiByCategory(String country,String category,int pageNumber) {
        if(!mIsPerformingQuery) {
            if(pageNumber == 0) {
                pageNumber = 1;
            }
            this.country = country;
            this.category = category;
            this.pageNumber = pageNumber;
            mIsQueryExhausted = false;
            setSearchCategoryType();
            executeQuery();
        }
    }

    public void searchArticlesApi(String query,int pageNumber) {
        if(!mIsPerformingQuery) {
            if(pageNumber == 0) {
                pageNumber = 1;
            }
            this.query = query;
            this.pageNumber = pageNumber;
            mIsQueryExhausted = false;
            setSearchQueryType();
            executeQuery();
        }
    }

    public void searchNextPage() {
        if(!mIsPerformingQuery && !mIsQueryExhausted) {
            pageNumber++;
            executeQuery();
        }
    }

    private void executeQuery() {
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        mIsPerformingQuery = true;
        viewState.setValue(ViewState.ARTICLES);
        final LiveData<Resource<List<Article>>> articlesSource;
        if(searchType.getValue() == SearchType.CATEGORY) {
            articlesSource = articleRepository.searchArticlesByCategories(country, category, pageNumber);
            Log.d(TAG,"executeQuery: Searching articles through category");
        } else if(searchType.getValue() == SearchType.QUERY) {
            articlesSource = articleRepository.searchArticlesApi(query, pageNumber);
            Log.d(TAG,"executeQuery: Searching articles through query");
        } else {
            articlesSource = articleRepository.searchArticlesApi(query, pageNumber);
        }
        articles.addSource(articlesSource,new Observer<Resource<List<Article>>>() {
            @Override
            public void onChanged(Resource<List<Article>> listResource) {
                if(!cancelRequest) {
                    if(listResource != null) {
                        articles.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS) {
                            Log.d(TAG,"onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000);
                            mIsPerformingQuery = false;
                            if(listResource.data != null) {
                                if(listResource.data.size() == 0) {
                                    Log.d(TAG,"onChanged: query is empty");
                                    articles.setValue(new Resource<List<Article>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            NO_RESULTS
                                    ));
                                }
                            }
                            articles.removeSource(articlesSource);
                        } else if(listResource.status == Resource.Status.ERROR) {
                            Log.d(TAG,"onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000);
                            mIsPerformingQuery = false;
                            articles.removeSource(articlesSource);
                        }
                    } else {
                        articles.removeSource(articlesSource);
                    }
                } else {
                    articles.removeSource(articlesSource);
                }

            }
        });
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

    public void cancelSearchRequest() {
        if(mIsPerformingQuery) {
            Log.d(TAG,"cancelSearchRequest: cancelling the search request...");
            cancelRequest = true;
            mIsPerformingQuery = false;
            pageNumber = 1;
        }
    }

}
