package com.example.toja.worldnewscache;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toja.worldnewscache.adapters.ArticleRecyclerAdapter;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.Constants;
import com.example.toja.worldnewscache.utils.VerticalSpacingItemDecorator;
import com.example.toja.worldnewscache.viewmodels.ArticleListViewModel;

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
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);
        mAdapter = new ArticleRecyclerAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnCategoryClickListener(categoryItemClickListener);
        mAdapter.setOnArticleClickListener(articleItemClickListener);
    }

    private void initSearchView() {
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void startArticleActivity(Article article) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

}
