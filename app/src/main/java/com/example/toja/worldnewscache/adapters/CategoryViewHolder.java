package com.example.toja.worldnewscache.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.toja.worldnewscache.R;
import com.example.toja.worldnewscache.responses.models.Article;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    CircleImageView categoryImage;
    TextView categoryTitle;
    private static View.OnClickListener mOnItemClickListener;
    private RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        this.requestManager = requestManager;

        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }

    public void onBind(Article article) {
        categoryTitle.setText(article.getTitle());

        Uri path = Uri.parse("android.resource://com.example.toja.worldnewscache/drawable/" + article.getUrlToImage());
        requestManager
                .load(path)
                .into(categoryImage);
    }

    public static void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
