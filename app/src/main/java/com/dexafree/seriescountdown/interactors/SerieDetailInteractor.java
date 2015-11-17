package com.dexafree.seriescountdown.interactors;

import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieDetail;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SerieDetailInteractor {

    @Inject
    ApiService apiService;

    @Inject
    public SerieDetailInteractor(ApiService apiService){
        this.apiService = apiService;
    }

    public Subscription loadSerieDetails(Serie serie, Observer<SerieDetail> observer){

        String query = serie.getCodeName();

        return apiService.getDetails(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

}
