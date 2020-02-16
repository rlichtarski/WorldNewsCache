package com.example.toja.worldnewscache;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.toja.worldnewscache.responses.models.Article;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = "ArticleActivity";

    private TextView articleTitle, articleAuthor, articlePublishedDate, articleDescription, articleUrl;
    private AppCompatImageView articleImage;
    private ScrollView scrollView;
    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        articleTitle = findViewById(R.id.article_title);
        articleAuthor = findViewById(R.id.article_author);
        articlePublishedDate = findViewById(R.id.article_published_date);
        articleDescription = findViewById(R.id.article_description);
        articleUrl = findViewById(R.id.article_url);
        articleImage = findViewById(R.id.article_image);
        scrollView = findViewById(R.id.parent);

        getIncomingIntent();
        setArticleViewProperties();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("article")) {
            Article article = getIntent().getParcelableExtra("article");
            mArticle = article;
            Log.d(TAG,"article title: " + article.getTitle());
        }
    }

    private void setArticleViewProperties() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.no_image);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(mArticle.getUrlToImage())
                .into(articleImage);

        articleTitle.setText(mArticle.getTitle());
        articlePublishedDate.setText("Date: " + mArticle.getPublishedAt());

        if (mArticle.getAuthor() == null) {
            articleAuthor.setText("Author: Unknown");
        } else {
            articleAuthor.setText("Author: " + mArticle.getAuthor());
        }

        SpannableString underlineUrl = new SpannableString(mArticle.getUrl());
        underlineUrl.setSpan(new UnderlineSpan(),0,mArticle.getUrl().length(),0);   //make article's url underlined
        articleUrl.setText(underlineUrl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            articleDescription.setText(Html.fromHtml(mArticle.getDescription(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            articleDescription.setText(Html.fromHtml(mArticle.getDescription()));
        }

        articleUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(mArticle.getUrl()));
                startActivity(browserIntent);
            }
        });

        showParent();
    }

    private void showParent() {
        scrollView.setVisibility(View.VISIBLE);
    }
}
