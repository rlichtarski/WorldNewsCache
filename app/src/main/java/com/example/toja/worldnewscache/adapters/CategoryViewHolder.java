package com.example.toja.worldnewscache.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toja.worldnewscache.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    CircleImageView categoryImage;
    TextView categoryTitle;
    private static View.OnClickListener mOnItemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }

    public static void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
