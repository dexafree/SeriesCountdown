package com.dexafree.seriescountdown.presenters;

import android.util.Log;

import com.dexafree.seriescountdown.interactors.SerieDetailInteractor;
import com.dexafree.seriescountdown.interfaces.DetailView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieInfo;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Created by Carlos on 2/9/15.
 */
public class DetailPresenter implements SerieDetailInteractor.Callback {

    private DetailView view;
    private SerieDetailInteractor interactor;

    public DetailPresenter(DetailView view) {
        this.view = view;
        this.interactor = new SerieDetailInteractor(this);
    }

    public void init(){
        Serie serie = view.getSerie();
        view.showProgress();
        interactor.loadSerieInfo(serie);

    }

    @Override
    public void onSerieInfoDownloaded(SerieInfo info){
        view.hideProgress();

        String timeUntilNextEpisode = getTimeUntilNextEpisode(info.getRemaining());

        view.showTimeRemaining(timeUntilNextEpisode);
        view.showNextEpisodeDate(info.getDateNextEpisode());
        view.showNExtEpisodeNumber(info.getNextEpisode());
    }

    private String getTimeUntilNextEpisode(String dateNextEpisode){

        String pattern = "MMMMM dd, yyyy HH:mm:ss";

        DateTime emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(dateNextEpisode).toDateTime();
        DateTime currentTime = DateTime.now();

        Period timeRemaining = new Period(currentTime, emissionTime).toPeriod();

        int days = Days.daysBetween(currentTime.toLocalDate(), emissionTime.toLocalDate()).getDays();
        int hours = timeRemaining.getHours();
        int minutes = timeRemaining.getMinutes();

        return days + " Days "+hours+" Hours "+minutes+" Minutes";

    }

    @Override
    public void onError() {
        view.hideProgress();
        view.showError();
    }
}
