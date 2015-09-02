package com.dexafree.seriescountdown.database.tables;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.dexafree.seriescountdown.model.Serie;

/**
 * Created by Carlos on 2/9/15.
 */
public class FavoriteSeriesTable {

    @NonNull
    public static final String TABLE = "series";

    @NonNull
    public static final String COLUMN_ID = "id";

    @NonNull
    public static final String COLUMN_NAME = "name";

    @NonNull
    public static final String COLUMN_CODENAME = "codename";

    @NonNull
    public static final String IMAGE_URL = "image_url";

    @NonNull
    public static final String QUERY_ALL = "SELECT * FROM "+TABLE;

    // This is just class with Meta Data, we don't need instances
    private FavoriteSeriesTable() {
        throw new IllegalStateException("No instances please");
    }

    // Better than static final field -> allows VM to unload useless String
    // Because you need this string only once per application life on the device
    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_CODENAME + " TEXT NOT NULL, "
                + IMAGE_URL + " TEXT NOT NULL"
                + ");";
    }

    public static Serie fromCursor(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String codeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODENAME));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL));

        return new Serie(name, codeName, imageUrl);
    }

    public static String queryForSerie(Serie serie){
        return "SELECT * FROM "+TABLE+" WHERE "+COLUMN_NAME + " = \""+serie.getName()+"\"";
    }

    public static String deleteQueryForSerie(Serie serie){
        return "DELETE FROM "+TABLE+" WHERE "+COLUMN_NAME+" = \""+serie.getName()+"\"";
    }


}
