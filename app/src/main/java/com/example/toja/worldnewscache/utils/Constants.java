package com.example.toja.worldnewscache.utils;

public class Constants {

    public static final String NEWS_BASE_URL = "https://newsapi.org/";

    public static final String PAGE_SIZE = "15";

    public static final String API_KEY = "49ab604c1aee4ec38922b493786e454b";

    public static final int NETWORK_TIMEOUT = 3000;

    public static final int ARTICLE_TYPE = 1;

    public static final int LOADING_TYPE = 2;

    public static final int CATEGORY_TYPE = 3;

    public static final int NETWORK_TIMEOUT_TYPE = 4;

    public static final int EXHAUSTED_TYPE = 5;

    public static final String CATEGORY_SEARCH_TYPE = "CATEGORY";

    public static final String QUERY_SEARCH_TYPE = "QUERY";

    public static final String[] SEARCH_CATEGORIES =
            {"Business", "Technology", "Sports", "Entertainment", "Science", "Health"};

    public static final String[] SEARCH_CATEGORY_IMAGES =
            {
                    "business_icon",
                    "technology_icon",
                    "sport_icon",
                    "entertainment_icon",
                    "science_icon",
                    "health_icon"
            };
}
