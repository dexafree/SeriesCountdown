package com.dexafree.seriescountdown.presenters;

import android.os.Handler;
import android.util.Log;

import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SerieDetailInteractor;
import com.dexafree.seriescountdown.interactors.SerieDetailNewApiInteractor;
import com.dexafree.seriescountdown.interfaces.DetailView;
import com.dexafree.seriescountdown.model.CountDown;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieDetail;
import com.dexafree.seriescountdown.model.SerieInfo;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Carlos on 2/9/15.
 */
public class DetailPresenter implements SerieDetailNewApiInteractor.Callback {

    private DetailView view;
    private SerieDetailNewApiInteractor interactor;
    private FavoriteSeriesInteractor favoriteSeriesInteractor;

    public DetailPresenter(DetailView view) {
        this.view = view;
        this.interactor = new SerieDetailNewApiInteractor(this);
        this.favoriteSeriesInteractor = new FavoriteSeriesInteractor(view);
    }

    public void init(){
        Serie serie = view.getSerie();

        boolean isSerieInserted = favoriteSeriesInteractor.isSerieInserted(serie);

        view.setFavoritable(!isSerieInserted);

        view.showProgress();
        interactor.loadSerieDetails(serie);

    }

    @Override
    public void onDataDownloaded(SerieDetail data) {
        view.hideProgress();


        String timeUntilNextEpisode = getTimeUntilNextEpisode(data.getAirDate());
        String airDateFormatted = formatAirDate(data.getAirDate());
        String nextEpisode = formatNextEpisode(data);
        String nextEpisodeOrder = getNextEpisodeOrder(data);
        String startDate = formatStartEndDate(data.getStartDate());
        String endDate = formatStartEndDate(data.getEndDate());

        view.showTimeRemaining(timeUntilNextEpisode);
        view.showNextEpisodeDate(airDateFormatted);
        view.showNextEpisodeInfo(nextEpisode, nextEpisodeOrder);
        view.showSerieStart(startDate);
        view.showSerieEnd(endDate);
        view.showSerieGenres(data.getGenres());
        view.showSerieDescription(data.getDescription().replace("<br>", "\n").trim());


    }

    /*//@Override
    public void onSerieInfoDownloaded(SerieInfo info){
        view.hideProgress();

        String timeUntilNextEpisode = getTimeUntilNextEpisode(info.getRemaining());

        view.showTimeRemaining(timeUntilNextEpisode);
        view.showNextEpisodeDate(info.getDateNextEpisode());
        //view.showNextEpisodeNumber(info.getNextEpisode());
        view.showSerieStart(info.getStart());
        view.showSerieEnd(info.getEnd());
        view.showSerieGenres(info.getGenres());
    }*/

    private String formatStartEndDate(String date){
        if(date.equals("Unknown")){
            return date;
        }

        String pattern = "MMM/dd/yyyy";
        DateTime emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(date).toDateTime();

        int day = emissionTime.getDayOfMonth();
        int month = emissionTime.getMonthOfYear();
        int year = emissionTime.getYear();

        return day + "/" + month + "/" + year;

    }

    private String getNextEpisodeOrder(SerieDetail data) {
        int season = data.getSeason();

        if(season == 0){
            return "Next episode";
        }

        return "Next episode  (S" + zeroPad(season) + " E" + zeroPad(data.getEpisode())+")";
    }

    private String formatNextEpisode(SerieDetail detail){

        String episodeName = detail.getEpisodeName();

        if(episodeName.equals("Unknown")){

            int season = detail.getSeason();

            if(season == 0){
                return episodeName;
            }

            return "Season " + season + " Episode " + detail.getEpisode();
        } else if (episodeName.equals("TBA")){
            return "To Be Announced";
        }

        return episodeName;

    }

    private String formatAirDate(String airDate){
        if(airDate.equals("Unknown")){
            return airDate;
        }

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTime emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(airDate).toDateTime();

        //String month = emissionTime.monthOfYear().getAsShortText(Locale.US);
        int month = emissionTime.getMonthOfYear();

        int day = emissionTime.getDayOfMonth();

        int year = emissionTime.getYear();

        String date = day + "/" + month + "/" + year;
        return date;

    }

    private String getTimeUntilNextEpisode(String dateNextEpisode){

        if(dateNextEpisode.equals("Unknown")){
            return "Unavailable";
        }

        String pattern = "yyyy-MM-dd HH:mm:ss";

        DateTime emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(dateNextEpisode).toDateTime();
        DateTime currentTime = DateTime.now();

        Period timeRemaining = new Period(currentTime, emissionTime).toPeriod();

        int days = Days.daysBetween(currentTime.toLocalDate(), emissionTime.toLocalDate()).getDays();
        int hours = timeRemaining.getHours();
        int minutes = timeRemaining.getMinutes();

        return days + " Days "+hours+" Hours "+minutes+" Minutes";

    }

    public void onSaveSerieClicked(){
        Serie serie = view.getSerie();

        if(favoriteSeriesInteractor.isSerieInserted(serie)){
            favoriteSeriesInteractor.deleteSerie(serie);
            view.setFavoritable(true);
        } else {
            favoriteSeriesInteractor.saveSerie(serie);
            view.setFavoritable(false);
        }

    }

    private String zeroPad(int number){
        return number < 10 ? "0"+number : ""+number;
    }

    @Override
    public void onError() {
        view.hideProgress();
        view.showError();
    }
}
