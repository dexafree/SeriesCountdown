package com.dexafree.seriescountdown.presenters;

import com.dexafree.seriescountdown.interfaces.SeriesView;

/**
 * Created by Carlos on 3/9/15.
 */
public abstract class BaseSerieListPresenter {

    protected SeriesView view;


    public BaseSerieListPresenter(SeriesView view){
        this.view = view;
    }

    public void init(){

    }

}
