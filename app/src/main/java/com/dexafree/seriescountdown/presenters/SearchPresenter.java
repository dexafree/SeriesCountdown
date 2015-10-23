package com.dexafree.seriescountdown.presenters;


import com.dexafree.seriescountdown.interactors.SearchSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;
import com.dexafree.seriescountdown.interfaces.ISearchView;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

import rx.Observer;
import rx.Subscription;

public class SearchPresenter {

    private ISearchView view;
    private SearchSuggestionsInteractor searchSuggestionsInteractor;
    private SearchSeriesInteractor getSeriesInteractor;

    private Subscription getSeriesSubscription;
    private Subscription getSuggestionsSubscription;

    private int searchPage;
    private boolean suggestionClicked;

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
            if(view.isShowingSerie(serie)){
                searchPage = 0;
            } else {
                view.addSerie(serie);
            }
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
            if(!suggestionClicked) {
                view.showSuggestions(strings);
            } else {
                suggestionClicked = false;
                view.hideSuggestions();
            }
        }
    };

    public SearchPresenter(ISearchView view) {
        this.view = view;
        this.searchSuggestionsInteractor = new SearchSuggestionsInteractor();
        this.getSeriesInteractor = new SearchSeriesInteractor();
        this.searchPage = 1;
        this.suggestionClicked = false;
    }

    public void onTextChanged(String text){
        this.getSuggestionsSubscription = searchSuggestionsInteractor.loadSuggestions(suggestionsObserver, text);

    }

    public void onScrollFinished(String query){
        if(searchPage >= 1) {
            if (getSeriesSubscription == null || getSeriesSubscription.isUnsubscribed()) {
                this.getSeriesSubscription = getSeriesInteractor.searchSeries(getSeriesObserver, query, searchPage++);
            }
        }
    }

    public void searchText(String text){
        view.cleanList();
        this.searchPage = 1;
        this.getSeriesSubscription = getSeriesInteractor.searchSeries(getSeriesObserver, text, searchPage++);
    }

    public void onSuggestionClicked(String text){
        this.suggestionClicked = true;
        searchText(text);
    }

    public void onItemClicked(int position){
        view.startDetailActivity(position);
    }
}
