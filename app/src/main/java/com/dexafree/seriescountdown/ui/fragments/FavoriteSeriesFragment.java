package com.dexafree.seriescountdown.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.presenters.FavoriteSeriesPresenter;

public class FavoriteSeriesFragment extends BaseSerieListFragment<FavoriteSeriesPresenter> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeriesCountdown.inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void reloadSeries(){

        // I don't know why it happens, but this is necessary
        if(presenter == null) {
            SeriesCountdown.inject(this);
        }
        presenter.reloadSeries();

    }


}
