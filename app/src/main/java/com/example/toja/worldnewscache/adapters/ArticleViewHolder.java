package com.example.toja.worldnewscache.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.toja.worldnewscache.R;
import com.example.toja.worldnewscache.responses.models.Article;


public class ArticleViewHolder extends RecyclerView.ViewHolder {

    TextView articleTitle, articleAuthor;
    AppCompatImageView articleImage;
    private static View.OnClickListener mOnItemClickListener;
    private RequestManager requestManager;

    public ArticleViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.article_title);
        articleAuthor = itemView.findViewById(R.id.article_author);
        articleImage = itemView.findViewById(R.id.article_image);

        this.requestManager = requestManager;

        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }

    public void onBind(Article article) {
        articleTitle.setText(article.getTitle());
        articleAuthor.setText(article.getAuthor());

        requestManager
                .load(article.getUrlToImage())
                .into(articleImage);
    }

    public static void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
