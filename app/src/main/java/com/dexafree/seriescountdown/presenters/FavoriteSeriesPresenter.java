package com.dexafree.seriescountdown.presenters;

import android.util.Log;

import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

import rx.Observer;
import rx.Subscription;

public class FavoriteSeriesPresenter extends BaseSerieListPresenter<FavoriteSeriesInteractor> implements Observer<Serie> {

    private Subscription subscription;

    public FavoriteSeriesPresenter(SeriesView view){
        super(view);
    }

    protected FavoriteSeriesInteractor getInteractor() {
        return new FavoriteSeriesInteractor();
    }

    @Override
    public void init() {
        loadSeries();
    }

    public void loadSeries() {
        this.subscription = interactor.loadSeries(this);
    }

    public void reloadSeries(){

        Observer<List<Serie>> reloadedSeries = new Observer<List<Serie>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.showError();
            }

            @Override
            public void onNext(List<Serie> series) {
                view.updateSeries(series);
            }
        };

        interactor.reloadSeries(reloadedSeries);
    }


    @Override
    public void onCompleted() {
        if(subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e("FAVORITESERIESINTERACTOR", e.getMessage());
        view.showError();
    }

    @Override
    public void onNext(Serie serie) {
        view.addItem(serie);
    }

}
