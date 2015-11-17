package com.dexafree.seriescountdown.interactors;

import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.dexafree.seriescountdown.model.Serie;
import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetPopularSeriesInteractor {

    private int maxPage = -1;

    @Inject
    ApiService apiService;

    @Inject
    public GetPopularSeriesInteractor(ApiService apiService){
        this.apiService = apiService;
    }

    public Subscription loadSeries(Observer<Serie> subscriber, int page) {

        if(maxPage == -1 ||  page <= maxPage) {

            return apiService.getPopularSeries(page)
                    .map(response -> {
                        this.maxPage = response.getMaxPages();
                        return response.getTvShows();
                    })
                    .flatMap(Observable::from)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        } else {
            return null;
        }

    }
}
