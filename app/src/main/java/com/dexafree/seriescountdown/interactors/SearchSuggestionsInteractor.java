package com.dexafree.seriescountdown.interactors;

import android.net.Uri;

import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.interactors.service.ApiService;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchSuggestionsInteractor {

    @Inject
    ApiService apiService;

    public SearchSuggestionsInteractor(){
        SeriesCountdown.inject(this);
    }

    public Subscription loadSuggestions(Observer<List<String>> observer, String query){

        String encodedQuery = Uri.encode(query);

        return apiService.getSuggestions(encodedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
}
