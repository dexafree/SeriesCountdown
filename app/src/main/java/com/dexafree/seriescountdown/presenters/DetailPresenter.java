package com.dexafree.seriescountdown.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SerieDetailInteractor;
import com.dexafree.seriescountdown.interfaces.DetailView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieDetail;
import com.dexafree.seriescountdown.persistence.DetailPersistance;
import com.dexafree.seriescountdown.persistence.PersistableObject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import javax.inject.Inject;

public class DetailPresenter implements SerieDetailInteractor.Callback {

    private final static String TAG = DetailPresenter.class.getName();

    @Inject
    FavoriteSeriesInteractor favoriteSeriesInteractor;

    private DetailView view;
    private SerieDetailInteractor interactor;


    private SerieDetail showingDetail;

    public DetailPresenter(DetailView view) {
        this.view = view;
        this.interactor = new SerieDetailInteractor(this);

        SeriesCountdown.inject(this);
    }

    public void init(){
        Serie serie = view.getSerie();

        view.showProgress();
        checkSerieInserted(serie);
        interactor.loadSerieDetails(serie);

    }

    public void init(PersistableObject persistance){

        view.loadFullSizeImage();
        Serie serie = view.getSerie();

        if(persistance != null) {
            DetailPersistance realPersistance = (DetailPersistance) persistance;
            this.showingDetail = realPersistance.getDetail();
        }

        if(showingDetail != null) {

            Log.d(TAG, "Init with persistance.");
            Log.d(TAG, "Persistance: " + showingDetail.toString());

            showSerieDetail(showingDetail);
            checkSerieInserted(serie);
            view.makeContentVisible();
        } else {
            init();
        }
    }

    private void checkSerieInserted(Serie serie){
        boolean isSerieInserted = favoriteSeriesInteractor.isSerieInserted(serie);

        view.setFavoritable(!isSerieInserted);
    }

    @Override
    public void onDataDownloaded(SerieDetail data) {

        this.showingDetail = data;
        showSerieDetail(data);

    }

    private void showSerieDetail(SerieDetail data){
        view.hideProgress();

        String timeUntilNextEpisode = getTimeUntilNextEpisode(data.getAirDate());
        String airDateFormatted = formatAirDate(data.getAirDate());
        String description = formatDescription(data.getDescription());
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
        view.showSerieDescription(description);
    }


    private String formatDescription(String description){

        String trimmed = description.replace("\n", "").replace("<br>", "").trim();

        if(TextUtils.isEmpty(trimmed)){
            return "No description available";
        }

        return description.replace("<br>", "\n").trim();
    }

    private String formatStartEndDate(String date){
        if(date.equals("Unknown")){
            return date;
        }

        DateTime emissionTime;

        // TRY-CATCH required for different formats provided (ie: The Flash and Game Of Thrones)
        // Is ugly, I know, but there are many different formats and it's impossible to check them all
        try {
            String pattern = "MMM/dd/yyyy";
            emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(date).toDateTime();
        } catch (IllegalArgumentException exception){

            try {
                String pattern = "yyyy-MM-dd";
                emissionTime = DateTimeFormat.forPattern(pattern).withLocale(Locale.US).parseDateTime(date).toDateTime();
            } catch (IllegalArgumentException e){
                return date;
            }
        }

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

        int month = emissionTime.getMonthOfYear();

        int day = emissionTime.getDayOfMonth();

        int year = emissionTime.getYear();

        return day + "/" + month + "/" + year;

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

    public DetailPersistance getPersistance(){
        return new DetailPersistance(showingDetail);
    }
}
