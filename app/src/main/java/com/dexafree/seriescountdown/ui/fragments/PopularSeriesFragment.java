package com.dexafree.seriescountdown.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.presenters.PopularSeriesPresenter;

public class PopularSeriesFragment extends BaseSerieListFragment<PopularSeriesPresenter> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeriesCountdown.inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void listScrollFinished() {
        super.listScrollFinished();
        presenter.onListScrollFinished();
    }
}
