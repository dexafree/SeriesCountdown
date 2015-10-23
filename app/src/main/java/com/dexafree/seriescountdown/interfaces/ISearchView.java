package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

public interface ISearchView extends IBaseView {


    void showSuggestions(List<String> suggestions);
    void hideSuggestions();
    void addSerie(Serie serie);
    void cleanList();
    void showSearchError();

    boolean isShowingSerie(Serie serie);
    void startDetailActivity(int position);

}
