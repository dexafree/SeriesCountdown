package com.dexafree.seriescountdown.interactors;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dexafree.seriescountdown.database.DatabaseOpenHelper;
import com.dexafree.seriescountdown.database.tables.FavoriteSeriesTable;
import com.dexafree.seriescountdown.interfaces.IBaseView;
import com.dexafree.seriescountdown.model.Serie;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Carlos on 2/9/15.
 */
public class FavoriteSeriesInteractor extends BaseSeriesInteractor {

    private SQLiteDatabase database;

    public FavoriteSeriesInteractor(IBaseView view) {
        this.database = new DatabaseOpenHelper(view.getContext()).getWritableDatabase();
    }

    public Subscription loadSeries(Observer<Serie> observer) {

        Cursor cursor = database.rawQuery(FavoriteSeriesTable.QUERY_ALL, null);

        return Observable.just(cursor)
                .map(this::mapCursorToSeriesList)
                .flatMapIterable(series -> series)
                .asObservable()
                .subscribe(observer);

    }

    public void reloadSeries(Observer<List<Serie>> observer){
        Cursor cursor = database.rawQuery(FavoriteSeriesTable.QUERY_ALL, null);

        Observable.just(cursor)
            .map(this::mapCursorToSeriesList)
            .asObservable()
            .subscribe(observer);
    }


    public boolean isSerieInserted(Serie serie){

        //Log.d("FAVORITESERIESINTERACTOR", "BEFORE ISSERIEINSERTED");
        Cursor cursor = database.rawQuery(FavoriteSeriesTable.queryForSerie(serie), null);
        int numSeries = mapCursorToSeriesList(cursor).size();

        //Log.d("FAVORITESERIESINTERACTOR", "AFTER ISSERIEINSERTED");

        return numSeries > 0;
    }

    public void saveSerie(Serie serie){

        if(!isSerieInserted(serie)) {
            ContentValues values = new ContentValues();
            values.put(FavoriteSeriesTable.COLUMN_NAME, serie.getName());
            values.put(FavoriteSeriesTable.COLUMN_CODENAME, serie.getCodeName());
            values.put(FavoriteSeriesTable.IMAGE_URL, serie.getImageUrl());

            //Log.d("FAVORITESERIESINTERACTOR", "BEFORE SAVE_SERIE");
            database.insert(FavoriteSeriesTable.TABLE, null, values);
            //Log.d("FAVORITESERIESINTERACTOR", "AFTER SAVE_SERIE");
        }
    }

    public void deleteSerie(Serie serie){
        //Log.d("FAVORITESERIESINTERACTOR", "BEFORE DELETE_SERIE");
        database.delete(FavoriteSeriesTable.TABLE, FavoriteSeriesTable.COLUMN_NAME + " = ?", new String[]{serie.getName()});
        database.rawQuery(FavoriteSeriesTable.deleteQueryForSerie(serie), null);
        //Log.d("FAVORITESERIESINTERACTOR", "AFTER DELETE_SERIE");

    }

    private List<Serie> mapCursorToSeriesList(Cursor cursor){

        List<Serie> series = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                series.add(FavoriteSeriesTable.fromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return series;

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        database.close();
    }
}
