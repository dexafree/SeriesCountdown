package com.dexafree.seriescountdown.presenters;

import android.util.Log;

import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;
import com.dexafree.seriescountdown.interfaces.ISearchView;

import java.util.List;

import rx.Observer;

/**
 * Created by Carlos on 5/9/15.
 */
public class SearchPresenter implements Observer<List<String>> {

    private ISearchView view;
    private SearchSuggestionsInteractor searchSuggestionsInteractor;

    public SearchPresenter(ISearchView view) {
        this.view = view;
        this.searchSuggestionsInteractor = new SearchSuggestionsInteractor();
    }

    public void onTextChanged(String text){
        Log.d("SEARCHPRESENTER", "RECEIVED QUERY: "+text);
        searchSuggestionsInteractor.loadSuggestions(text, this);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<String> strings) {

        view.showSuggestions(strings);
    }
}
