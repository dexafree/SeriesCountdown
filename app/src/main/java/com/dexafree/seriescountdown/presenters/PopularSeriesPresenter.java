package com.dexafree.seriescountdown.presenters;

import android.util.Log;

import com.dexafree.seriescountdown.interactors.GetSeriesInteractor;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Carlos on 2/9/15.
 */
public class PopularSeriesPresenter extends BaseSerieListPresenter<GetSeriesInteractor> implements Observer<Serie> {

    private Subscription subscription;
    private int currentPage;

    public PopularSeriesPresenter(SeriesView view){
        super(view);
        this.currentPage = 1;
    }

    public GetSeriesInteractor getInteractor() {
        return new GetSeriesInteractor();
    }

    @Override
    public void init() {
        this.subscription = interactor.loadSeries(this, currentPage++);
    }


    public void onListScrollFinished() {
        this.subscription = interactor.loadSeries(this, currentPage++);
    }

    @Override
    public void onCompleted() {
        if (subscription != null) {

            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.d("POPULARSERIESPRESENTER", "ERROR");
        view.showError();
    }

    @Override
    public void onNext(Serie serie) {
        view.addItem(serie);
    }
}
