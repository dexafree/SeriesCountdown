package com.dexafree.seriescountdown.interfaces;

import com.dexafree.seriescountdown.model.Serie;

public interface DetailView {

    void showProgress();
    void hideProgress();

    void makeContentVisible();
    void loadFullSizeImage();

    void setFavoritable(boolean favoritable);

    void showTimeRemaining(String text);
    void showNextEpisodeDate(String text);
    void showNextEpisodeInfo(String title, String subtitle);
    void showSerieStart(String text);
    void showSerieEnd(String text);
    void showSerieGenres(String text);
    void showSerieDescription(String text);

    void showError();

    Serie getSerie();

}
