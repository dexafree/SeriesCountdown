package com.dexafree.seriescountdown.presenters;


import com.dexafree.seriescountdown.interactors.GetSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;
import com.dexafree.seriescountdown.interfaces.ISearchView;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Carlos on 5/9/15.
 */
public class SearchPresenter {

    private ISearchView view;
    private SearchSuggestionsInteractor searchSuggestionsInteractor;
    private GetSeriesInteractor getSeriesInteractor;

    private Subscription getSeriesSubscription;
    private Subscription getSuggestionsSubscription;

    private int searchPage;


    private final Observer<Serie> getSeriesObserver = new Observer<Serie>() {
        @Override
        public void onCompleted() {
            getSeriesSubscription.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            view.showSearchError();
        }

        @Override
        public void onNext(Serie serie) {
            view.addSerie(serie);
        }
    };

    private final Observer<List<String>> suggestionsObserver = new Observer<List<String>>() {
        @Override
        public void onCompleted() {
            getSuggestionsSubscription.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<String> strings) {
            view.showSuggestions(strings);
        }
    };

    public SearchPresenter(ISearchView view) {
        this.view = view;
        this.searchSuggestionsInteractor = new SearchSuggestionsInteractor();
        this.getSeriesInteractor = new GetSeriesInteractor();
        this.searchPage = 1;
    }

    public void onTextChanged(String text){
        this.getSuggestionsSubscription = searchSuggestionsInteractor.loadSuggestions(suggestionsObserver, text);

    }

    public void onScrollFinished(String query){
        if(getSeriesSubscription == null || getSeriesSubscription.isUnsubscribed()) {
            this.getSeriesSubscription = getSeriesInteractor.searchSeries(getSeriesObserver, query, searchPage++);
        }
    }

    public void searchText(String text){
        view.cleanList();
        this.getSeriesSubscription = getSeriesInteractor.searchSeries(getSeriesObserver, text, searchPage++);
    }

    public void onItemClicked(int position){

    }
}
