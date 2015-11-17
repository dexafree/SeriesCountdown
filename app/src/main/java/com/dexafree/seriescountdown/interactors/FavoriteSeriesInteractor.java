package com.dexafree.seriescountdown.interactors;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dexafree.seriescountdown.database.tables.FavoriteSeriesTable;
import com.dexafree.seriescountdown.model.Serie;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class FavoriteSeriesInteractor {

    @Inject
    SQLiteDatabase database;

    @Inject
    public FavoriteSeriesInteractor(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
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

        Cursor cursor = database.rawQuery(FavoriteSeriesTable.queryForSerie(serie), null);
        int numSeries = mapCursorToSeriesList(cursor).size();

        return numSeries > 0;
    }

    public void saveSerie(Serie serie){

        if(!isSerieInserted(serie)) {
            ContentValues values = new ContentValues();
            values.put(FavoriteSeriesTable.COLUMN_NAME, serie.getName());
            values.put(FavoriteSeriesTable.COLUMN_CODENAME, serie.getCodeName());
            values.put(FavoriteSeriesTable.IMAGE_URL, serie.getImageUrl());

            database.insert(FavoriteSeriesTable.TABLE, null, values);
        }
    }

    public void deleteSerie(Serie serie){
        database.delete(FavoriteSeriesTable.TABLE, FavoriteSeriesTable.COLUMN_NAME + " = ?", new String[]{serie.getName()});
        database.rawQuery(FavoriteSeriesTable.deleteQueryForSerie(serie), null);

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
