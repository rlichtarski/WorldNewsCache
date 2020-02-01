package com.example.toja.worldnewscache.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.toja.worldnewscache.R;
import com.example.toja.worldnewscache.responses.models.Article;
import com.example.toja.worldnewscache.utils.Constants;


import java.util.ArrayList;
import java.util.List;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> mArticles;
    public int mViewType;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {

        View view = null;

        switch (viewType) {
            case Constants.ARTICLE_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_item,parent,false);
                return new ArticleViewHolder(view);
            }
            case Constants.LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item,parent,false);
                return new LoadingViewHolder(view);
            }
            case Constants.CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item,parent,false);
                return new CategoryViewHolder(view);
            }
            case Constants.NETWORK_TIMEOUT_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timeout_list_item,parent,false);
                return new TimeoutViewHolder(view);
            }
            case Constants.EXHAUSTED_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_list_item,parent,false);
                return new ExhaustedQueryViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_item,parent,false);
                return new ArticleViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        mViewType = getItemViewType(position);
        if (mViewType == Constants.ARTICLE_TYPE) {
            ((ArticleViewHolder) holder).articleTitle.setText(mArticles.get(position).getTitle());
            ((ArticleViewHolder) holder).articleAuthor.setText(mArticles.get(position).getAuthor());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            if(mArticles.get(position).getUrlToImage() == null || mArticles.get(position).getUrlToImage().equals("")) {
                Glide.with(holder.itemView.getContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(R.drawable.no_image)
                        .into(((ArticleViewHolder) holder).articleImage);
            } else {
                Glide.with(holder.itemView.getContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(mArticles.get(position).getUrlToImage())
                        .into(((ArticleViewHolder) holder).articleImage);
            }
        } else if (mViewType == Constants.CATEGORY_TYPE) {
            ((CategoryViewHolder) holder).categoryTitle.setText(mArticles.get(position).getTitle());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://com.example.toja.worldnews/drawable/" + mArticles.get(position).getUrlToImage());
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) holder).categoryImage);
        }
    }

    public void setOnCategoryClickListener(View.OnClickListener onItemClickListener) {
        CategoryViewHolder.setOnItemClickListener(onItemClickListener);
    }

    public void setOnArticleClickListener(View.OnClickListener onItemClickListener) {
        ArticleViewHolder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        if (mArticles != null) {
            if (mArticles.get(position).getTitle().equals("LOADING...")) {
                return Constants.LOADING_TYPE;
            } else if (position == mArticles.size() - 1
                    && position != 0
                    && !mArticles.get(position).getUrl().equals("CATEGORIES...")
                    && !mArticles.get(position).getTitle().equals("EXHAUSTED...")) {
                return Constants.LOADING_TYPE;
            } else if (mArticles.get(position).getUrl().equals("CATEGORIES...")) {
                return Constants.CATEGORY_TYPE;
            } else if(mArticles.get(position).getTitle().equals("TIMEOUT...")) {
                return Constants.NETWORK_TIMEOUT_TYPE;
            } else if(mArticles.get(position).getTitle().equals("EXHAUSTED...")) {
                return Constants.EXHAUSTED_TYPE;
            } else {
                return Constants.ARTICLE_TYPE;
            }
        }
        return Constants.ARTICLE_TYPE;
    }

    public void displayQueryExhausted() {
        hideLoading();
        Article exhaustedArticle = new Article();
        exhaustedArticle.setTitle("EXHAUSTED...");
        exhaustedArticle.setUrl("e");  //url can't be empty while checking view types in getItemViewType method
        mArticles.add(exhaustedArticle);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if(isLoading()) {
            for (Article article : mArticles) {
                if(article.getTitle().equals("LOADING...")) {
                    mArticles.remove(article);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayNetworkTimeout() {
        Article article = new Article();
        article.setTitle("TIMEOUT...");
        article.setUrl("e");  //url can't be empty while checking view types in getItemViewType method
        List<Article> loadingList = new ArrayList<>();
        loadingList.add(article);
        mArticles = loadingList;
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (!isLoading()) {
            Article article = new Article();
            article.setTitle("LOADING...");
            List<Article> loadingList = new ArrayList<>();
            loadingList.add(article);
            mArticles = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mArticles != null) {
            if (mArticles.size() > 0) {
                return mArticles.get(mArticles.size() - 1).getTitle().equals("LOADING...");
            }
        }
        return false;
    }

    public void displaySearchCategories() {
        List<Article> categories = new ArrayList<>();
        for (int i = 0; i < Constants.SEARCH_CATEGORIES.length; i++) {
            Article article = new Article();
            article.setTitle(Constants.SEARCH_CATEGORIES[i]);
            article.setUrlToImage(Constants.SEARCH_CATEGORY_IMAGES[i]);
            article.setUrl("CATEGORIES...");
            categories.add(article);
        }
        mArticles = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mArticles != null) {
            return mArticles.size();
        }
        return 0;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    public Article getSelectedArticle(int position) {
        if(mArticles != null) {
            if (mArticles.size() > 0) {
                return mArticles.get(position);
            }
        }
        return null;
    }

}
