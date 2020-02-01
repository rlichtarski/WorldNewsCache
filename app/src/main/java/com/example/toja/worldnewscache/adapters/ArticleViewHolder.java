package com.example.toja.worldnewscache.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toja.worldnewscache.R;


public class ArticleViewHolder extends RecyclerView.ViewHolder {

    TextView articleTitle, articleAuthor;
    AppCompatImageView articleImage;
    private static View.OnClickListener mOnItemClickListener;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.article_title);
        articleAuthor = itemView.findViewById(R.id.article_author);
        articleImage = itemView.findViewById(R.id.article_image);

        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }

    public static void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
