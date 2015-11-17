package com.dexafree.seriescountdown.presenters;

import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;

import javax.inject.Inject;

import rx.Observer;

public abstract class BaseSerieListPresenter<T> implements Observer<Serie> {

    @Inject
    T interactor;

    protected SeriesView view;

    public BaseSerieListPresenter(SeriesView view){
        this.view = view;
    }

    public abstract void init();

    public void onItemClicked(int position){
        view.startDetailActivity(position);
    }

}
