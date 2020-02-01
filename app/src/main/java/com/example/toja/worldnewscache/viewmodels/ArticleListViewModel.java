package com.example.toja.worldnewscache.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ArticleListViewModel extends AndroidViewModel {

    private static final String TAG = "ArticleListViewModel";

    public enum ViewState {CATEGORIES, ARTICLES}

    private MutableLiveData<ViewState> viewState;

    public ArticleListViewModel(@NonNull Application application) {
        super(application);

        init();
    }

    private void init() {
        if(viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }

}
