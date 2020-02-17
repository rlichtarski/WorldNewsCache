package com.example.toja.worldnewscache;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.toja.worldnewscache.adapters.ArticleRecyclerAdapter;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.Constants;
import com.example.toja.worldnewscache.utils.Resource;
import com.example.toja.worldnewscache.utils.VerticalSpacingItemDecorator;
import com.example.toja.worldnewscache.viewmodels.ArticleListViewModel;

import java.util.List;

import static com.example.toja.worldnewscache.viewmodels.ArticleListViewModel.NO_RESULTS;
import static com.example.toja.worldnewscache.viewmodels.ArticleListViewModel.ViewState.ARTICLES;
import static com.example.toja.worldnewscache.viewmodels.ArticleListViewModel.ViewState.CATEGORIES;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SearchView mSearchView;
    private RecyclerView recyclerView;
    private ArticleRecyclerAdapter mAdapter;
    private ArticleListViewModel articleListViewModel;

    private View.OnClickListener articleItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Article article = mAdapter.getSelectedArticle(position);
            if(!mSearchView.isIconified()) {
                mSearchView.setIconified(true);
                mSearchView.onActionViewCollapsed();
                mSearchView.clearFocus();
            }
            Log.d(TAG,"onClick: position: " + position + "; title: " + article.getTitle());
            startArticleActivity(article);
        }
    };

    private View.OnClickListener categoryItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            String category = Constants.SEARCH_CATEGORIES[position];
            if(!mSearchView.isIconified()) {
                mSearchView.setIconified(true);
                mSearchView.onActionViewCollapsed();
                mSearchView.clearFocus();
            }
            mAdapter.displayLoading();
            searchArticlesApiByCategory(category);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleListViewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);

        initRecyclerView();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initSearchView();
        subscribeObservers();
    }

    private void initRecyclerView() {
        ViewPreloadSizeProvider<String> viewPreloadSizeProvider = new ViewPreloadSizeProvider<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);
        mAdapter = new ArticleRecyclerAdapter(initGlide(), viewPreloadSizeProvider);
        RecyclerViewPreloader<String> recyclerViewPreloader = new RecyclerViewPreloader<String>(Glide.with(this), mAdapter, viewPreloadSizeProvider, 15);

        recyclerView.addOnScrollListener(recyclerViewPreloader);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView,newState);

                if(!recyclerView.canScrollVertically(1) && articleListViewModel.getViewState().getValue() == ARTICLES) {
                    articleListViewModel.searchNextPage();
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnCategoryClickListener(categoryItemClickListener);
        mAdapter.setOnArticleClickListener(articleItemClickListener);
    }

    private void initSearchView() {
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchArticlesApi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private RequestManager initGlide() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
    }

    private void subscribeObservers() {
        articleListViewModel.getArticles().observe(this,new Observer<Resource<List<Article>>>() {
            @Override
            public void onChanged(Resource<List<Article>> listResource) {
                if(listResource != null) {
                    Log.d(TAG,"onChanged: status: " + listResource.status);

                    if(listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                                if(articleListViewModel.getPageNumber() > 1) {
                                    mAdapter.displayLoading();
                                } else {
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }

                            case ERROR: {
                                Log.d(TAG,"onChanged: status: ERROR " + listResource.message);
                                Log.d(TAG,"onChanged: cannot refresh the cache.");
                                Log.d(TAG,"onChanged: data size: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setArticles(listResource.data);
                                Toast.makeText(MainActivity.this,listResource.message,Toast.LENGTH_SHORT).show();

                                if(listResource.message.equals(NO_RESULTS)) {
                                    mAdapter.displayQueryExhausted();
                                }
                                break;
                            }

                            case SUCCESS: {
                                Log.d(TAG,"onChanged: cache has been refreshed.");
                                Log.d(TAG,"onChanged: status: SUCCESS, #Articles: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setArticles(listResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });

        articleListViewModel.getViewState().observe(this,new Observer<ArticleListViewModel.ViewState>() {
            @Override
            public void onChanged(ArticleListViewModel.ViewState viewState) {
                if(viewState != null) {
                    switch (viewState) {
                        case ARTICLES: {
                            break;
                        }
                        case CATEGORIES: {
                            displayCategories();
                        }
                    }
                }
            }
        });
    }

    private void searchArticlesApi(String query) {
        recyclerView.smoothScrollToPosition(0);
        articleListViewModel.searchArticlesApi(query, 1);
        mSearchView.clearFocus();
    }

    private void searchArticlesApiByCategory(String category) {
        recyclerView.smoothScrollToPosition(0);
        articleListViewModel.searchArticlesApiByCategory("us", category, 1);
        mSearchView.clearFocus();
    }

    private void displayCategories() {
        mAdapter.displaySearchCategories();
    }

    private void startArticleActivity(Article article) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(articleListViewModel.getViewState().getValue() == CATEGORIES) {
            super.onBackPressed();
        } else {
            if(!mSearchView.isIconified()) {
                mSearchView.setIconified(true);
                mSearchView.onActionViewCollapsed();
                mSearchView.clearFocus();
            }
            articleListViewModel.cancelSearchRequest();
            articleListViewModel.setViewCategories();
        }
    }
}
