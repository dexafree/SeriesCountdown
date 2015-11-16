package com.dexafree.seriescountdown.interactors;


import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.dexafree.seriescountdown.model.Serie;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchSeriesInteractor extends BaseSeriesInteractor {

    @Inject
    ApiService apiService;

    private String lastQuery;
    private int maxPages = -1;

    public SearchSeriesInteractor(){
        SeriesCountdown.inject(this);
    }

    public Subscription searchSeries(Observer<Serie> subscriber, String query, int page){

        if(!query.equalsIgnoreCase(lastQuery)
                || maxPages == -1
                || (query.equalsIgnoreCase(lastQuery) && page < maxPages)) {

            this.lastQuery = query;


            String sanitizedQuery = query.replaceAll(" ", "+");

            return apiService.searchSerie(sanitizedQuery, page)
                    .map(response -> {

                        if(response.getMaxPages() == 0){
                            throw new EmptySerieListException();
                        }

                        maxPages = response.getMaxPages();
                        return response.getTvShows();
                    })
                    .flatMap(Observable::from)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subscriber);
        }

        return null;
    }

    public static class EmptySerieListException extends RuntimeException {}

}
