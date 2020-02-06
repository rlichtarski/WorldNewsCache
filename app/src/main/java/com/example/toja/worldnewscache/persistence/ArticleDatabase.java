package com.example.toja.worldnewscache.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.toja.worldnewscache.responses.models.Article;

@Database(entities = {Article.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ArticleDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "articles_db";

    private static ArticleDatabase instance;

    public static ArticleDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ArticleDatabase.class,
                    DATABASE_NAME

            ).build();
        }
        return instance;
    }
}
