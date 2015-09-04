package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

/**
 * Created by Carlos on 1/9/15.
 */
public interface SeriesView extends IBaseView {

    void addItem(Serie serie);
    void updateSeries(List<Serie> series);
    void showError();
    void clearList();

    void startDetailActivity(int index);

}
