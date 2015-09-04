package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieInfo;

/**
 * Created by Carlos on 2/9/15.
 */
public interface DetailView extends IBaseView {

    void showProgress();
    void hideProgress();

    void setFavoritable(boolean favoritable);

    void showTimeRemaining(String text);
    void showNextEpisodeDate(String text);
    void showNextEpisodeNumber(String text);
    void showSerieStart(String text);
    void showSerieEnd(String text);
    void showSerieGenres(String text);

    void showError();

    Serie getSerie();

}
