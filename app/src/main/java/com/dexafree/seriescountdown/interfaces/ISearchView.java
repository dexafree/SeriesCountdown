package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

/**
 * Created by Carlos on 5/9/15.
 */
public interface ISearchView  {


    void showSuggestions(List<String> suggestions);
    void addSerie(Serie serie);
    void cleanList();

    void showSearchError();

}
