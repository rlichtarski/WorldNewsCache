package com.example.toja.worldnewscache.viewmodels;

import android.app.Application;
import android.provider.MediaStore;
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

    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Article>>> articles = new MediatorLiveData<>();

    private ArticleRepository articleRepository;

    private boolean mIsPerformingQuery;
    private boolean mIsQueryExhausted;
    private int pageNumber;
    private String query;

    public static final String QUERY_EXHAUSTED = "No more results";

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
    }

    public LiveData<Resource<List<Article>>> getArticles() {
        return articles;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void searchArticlesApi(String query,int pageNumber) {
        if(!mIsPerformingQuery) {
            if(pageNumber == 0) {
                pageNumber = 1;
            }
            this.query = query;
            this.pageNumber = pageNumber;
            mIsQueryExhausted = false;
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
        mIsPerformingQuery = true;
        viewState.setValue(ViewState.ARTICLES);
        final LiveData<Resource<List<Article>>> articlesSource = articleRepository.searchArticlesApi(query, pageNumber);

        articles.addSource(articlesSource,new Observer<Resource<List<Article>>>() {
            @Override
            public void onChanged(Resource<List<Article>> listResource) {
                if(listResource != null) {
                    articles.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS) {
                        mIsPerformingQuery = false;
                        if(listResource.data != null) {
                            if(listResource.data.size() == 0) {
                                Log.d(TAG,"onChanged: query is empty");
                                articles.setValue(new Resource<List<Article>>(
                                        Resource.Status.SUCCESS,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                            }
                        }
                        articles.removeSource(articlesSource);
                    } else if(listResource.status == Resource.Status.ERROR) {
                        mIsPerformingQuery = false;
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

}
