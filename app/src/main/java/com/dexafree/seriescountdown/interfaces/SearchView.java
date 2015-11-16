package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

public interface SearchView {


    void showSuggestions(List<String> suggestions);
    void hideSuggestions();
    void addSerie(Serie serie);
    void cleanList();
    void showSearchError();
    void showNoResults();

    void showLoading();
    void hideLoading();

    boolean isShowingSerie(Serie serie);
    void startDetailActivity(int position);

}
