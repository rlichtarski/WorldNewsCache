package com.example.toja.worldnewscache.utils;

import android.util.Log;

import com.example.toja.worldnewscache.responses.models.Article;

import java.util.List;

public class TestingObservers {
    public static void printArticles(String tag,List<Article> articles) {
        for(Article article : articles) {
            Log.d(tag, "article title: " + article.getTitle());
        }
    }
}
