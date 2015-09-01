package com.dexafree.seriescountdown.presenters;

import com.dexafree.seriescountdown.interactors.BaseSeriesInteractor;
import com.dexafree.seriescountdown.interactors.PopularSeriesInteractor;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

/**
 * Created by Carlos on 1/9/15.
 */
public class MainPresenter implements BaseSeriesInteractor.Callback {

    private SeriesView view;
    private BaseSeriesInteractor interactor;

    public MainPresenter(SeriesView view) {
        this.view = view;
        this.interactor = new PopularSeriesInteractor(this);
    }

    public void init(){
        interactor.loadSeries();
    }

    @Override
    public void onSeriesDownloaded(List<Serie> series) {
        view.showSeries(series);
    }

    @Override
    public void onError() {
        view.showError();
    }
}
