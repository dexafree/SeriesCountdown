package com.dexafree.seriescountdown.ui.fragments;

import android.util.Log;

import com.dexafree.seriescountdown.presenters.FavoriteSeriesPresenter;

/**
 * Created by Carlos on 3/9/15.
 */
public class FavoriteSeriesFragment extends BaseSerieListFragment<FavoriteSeriesPresenter> {

    @Override
    protected FavoriteSeriesPresenter getPresenter() {
        return new FavoriteSeriesPresenter(this);
    }

    public void reloadSeries(){
        if(presenter != null) {
            presenter.reloadSeries();
        } else {
            Log.d("FAVORITESERIESFRAGMENT", "PRESENTER IS NULL");
        }
    }


}
