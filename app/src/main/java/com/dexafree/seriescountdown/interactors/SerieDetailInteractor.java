package com.dexafree.seriescountdown.interactors;

import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Carlos on 2/9/15.
 */
public class SerieDetailInteractor {

    private final static String BASE_URL = "http://www.episodate.com/tv-show/";

    private final static String NEXT_EPISODE_SELECTOR = "b.color-green";
    private final static String NEXT_EPISODE_DATE_SELECTOR = "b.episode_datetime_convert";
    private final static String NEXT_EPISODE_REMAINING = ".countdownTime";
    private final static String SERIE_CATEGORIES = "div > div > div.row.text-size-15.line-height-200.text-left > div > div";


    public interface Callback {
        void onSerieInfoDownloaded(SerieInfo info);
        void onError();
    }

    private Callback callback;

    public SerieDetailInteractor(Callback callback) {
        this.callback = callback;
    }

    public void loadSerieInfo(Serie serie){
        AsyncJob.doInBackground(() -> {

            try {
                Document doc = Jsoup.connect(BASE_URL + serie.getCodeName())
                        .timeout(5000)
                        .get();

                parseDocument(doc);

            } catch (IOException e){
                e.printStackTrace();
                sendError();
            }


        });
    }

    private void parseDocument(Document doc){

        String nextEpisode = "Unavailable";

        Element nextEpisodeElement = doc.select(NEXT_EPISODE_SELECTOR).first();

        if(nextEpisodeElement != null){
            nextEpisode = nextEpisodeElement.text();
        }

        String nextEpisodeDate = "Unknown";

        Element nextEpisodeDateElement = doc.select(NEXT_EPISODE_DATE_SELECTOR).first();

        if(nextEpisodeDateElement != null){
            nextEpisodeDate = nextEpisodeDateElement.text();
        }

        Elements categories = doc.select(SERIE_CATEGORIES);

        Element genreElement = categories.get(0);
        Element statusElement = categories.get(2);
        Element startElement = categories.get(3);
        Element endElement = categories.get(4);
        Element timeRemainingNext = doc.select(NEXT_EPISODE_REMAINING).first();

        String genre = genreElement.text();
        String status = statusElement.text();
        String start = startElement.text();
        String end = endElement.text();


        String timeRemainingNextEpisode = null;

        if(timeRemainingNext != null){
            timeRemainingNextEpisode = timeRemainingNext.attr("data-date");
        }


        Log.d("SERIEDETAILINTERACTOR", "TIME REMAINING: "+timeRemainingNextEpisode);

        SerieInfo info = new SerieInfo(genre, status, start, end, timeRemainingNextEpisode, nextEpisode, nextEpisodeDate);
        sendResults(info);

    }

    private void sendResults(SerieInfo info){
        AsyncJob.doOnMainThread(() -> callback.onSerieInfoDownloaded(info));
    }

    private void sendError(){
        AsyncJob.doOnMainThread(callback::onError);
    }
}
