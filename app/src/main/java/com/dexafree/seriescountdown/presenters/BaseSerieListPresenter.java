package com.dexafree.seriescountdown.presenters;

import com.dexafree.seriescountdown.interactors.BaseSeriesInteractor;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;

import rx.Observer;

public abstract class BaseSerieListPresenter<T extends BaseSeriesInteractor> implements Observer<Serie> {

    protected SeriesView view;
    protected T interactor;

    public BaseSerieListPresenter(SeriesView view){
        this.view = view;
        this.interactor = getInteractor();
    }

    public abstract void init();

    public void onItemClicked(int position){
        view.startDetailActivity(position);
    }

    protected abstract T getInteractor();

}
