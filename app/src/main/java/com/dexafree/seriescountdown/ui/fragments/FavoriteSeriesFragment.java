package com.dexafree.seriescountdown.ui.fragments;

import com.dexafree.seriescountdown.presenters.FavoriteSeriesPresenter;

public class FavoriteSeriesFragment extends BaseSerieListFragment<FavoriteSeriesPresenter> {

    @Override
    protected FavoriteSeriesPresenter getPresenter() {
        return new FavoriteSeriesPresenter(this);
    }

    public void reloadSeries(){
        if(presenter != null) {
            presenter.reloadSeries();
        }
    }


}
