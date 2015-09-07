package com.dexafree.seriescountdown.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.dexafree.seriescountdown.database.tables.FavoriteSeriesTable;


public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Favorites.db";

    public DatabaseOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(FavoriteSeriesTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not implemented
    }
}