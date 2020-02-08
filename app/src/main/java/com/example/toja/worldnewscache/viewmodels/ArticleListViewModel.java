package com.example.toja.worldnewscache.viewmodels;

import android.app.Application;
import android.provider.MediaStore;

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

    public ArticleListViewModel(@NonNull Application application) {
        super(application);
        articleRepository = ArticleRepository.getInstance(application);
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

    public void searchArticlesApi(String query, int pageNumber) {
        final LiveData<Resource<List<Article>>> articlesSource = articleRepository.searchArticlesApi(query, pageNumber);

        articles.addSource(articlesSource,new Observer<Resource<List<Article>>>() {
            @Override
            public void onChanged(Resource<List<Article>> listResource) {
                articles.setValue(listResource);
            }
        });
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

}
