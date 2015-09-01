package com.dexafree.seriescountdown.interactors;

import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.dexafree.seriescountdown.model.Serie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Carlos on 1/9/15.
 */
public class PopularSeriesInteractor extends BaseSeriesInteractor {

    private final static String POPULAR_SERIES_ENDPOINT = "http://www.episodate.com/most-popular";

    private final static String BLOCK_SELECTOR = "div.mix-border > a";
    private final static String IMAGE_SELECTOR = "div.image-block";
    private final static String TITLE_SELECTOR = "span.sorting-cover > span";

    public PopularSeriesInteractor(Callback callback) {
        super(callback);
    }

    @Override
    public void loadSeries() {

        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {

                try {
                    Document doc = Jsoup.connect(POPULAR_SERIES_ENDPOINT)
                            .timeout(5000)
                            .get();

                    parseDocument(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError();
                }
            }
        });
    }

    private void parseDocument(Document doc){

        List<Serie> series = new ArrayList<>();

        Elements blocks = doc.select(BLOCK_SELECTOR);

        for(Element element : blocks){

            String name = getName(element);
            String codeName = getCodeName(element);
            String imageUrl = getImageUrl(element);

            series.add(new Serie(name, codeName, imageUrl));
        }

        sendResult(series);

    }

    private String getName(Element element){
        return element.select(TITLE_SELECTOR).text();
    }

    private String getCodeName(Element element){
        String href = element.attr("href");
        String[] splits = href.split("/");
        return splits[splits.length-1];
    }

    private String getImageUrl(Element element){
        Element imageBlock = element.select(IMAGE_SELECTOR).first();
        String style = imageBlock.attr("style");

        Pattern pattern = Pattern.compile("(http.*\\.jpg)");

        Matcher matcher = pattern.matcher(style);

        String imageUrl = "NONE";

        if(matcher.find()){
            imageUrl = matcher.group();
        }

        return imageUrl;

    }
}
