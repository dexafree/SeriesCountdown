package com.dexafree.seriescountdown.ui.fragments;

import com.dexafree.seriescountdown.presenters.PopularSeriesPresenter;

public class PopularSeriesFragment extends BaseSerieListFragment<PopularSeriesPresenter> {

    @Override
    protected PopularSeriesPresenter getPresenter() {
        return new PopularSeriesPresenter(this);
    }

    @Override
    protected void listScrollFinished() {
        super.listScrollFinished();
        presenter.onListScrollFinished();
    }
}
