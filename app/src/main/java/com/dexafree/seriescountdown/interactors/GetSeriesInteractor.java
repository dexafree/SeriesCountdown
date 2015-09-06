package com.dexafree.seriescountdown.interactors;

import com.dexafree.seriescountdown.model.Serie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Carlos on 1/9/15.
 */
public class GetSeriesInteractor extends BaseSeriesInteractor {


    private final static String POPULAR_SERIES_ENDPOINT = "http://www.episodate.com/most-popular?page=";
    private final static String SEARCH_SERIES_ENDPOINT = "http://www.episodate.com/search?q=%s&page=";

    private final static String BLOCK_SELECTOR = "div.mix-border > a";
    private final static String IMAGE_SELECTOR = "div.image-block";
    private final static String TITLE_SELECTOR = "span.sorting-cover > span";


    public Subscription loadSeries(Observer<Serie> subscriber, int page) {

        return getObservable(POPULAR_SERIES_ENDPOINT, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);

    }

    public Subscription searchSeries(Observer<Serie> subscriber, String query, int page){
        String url = SEARCH_SERIES_ENDPOINT.replace("%s", query);

        return getObservable(url, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    private Observable<Serie> getObservable(String endpoint, int page){

        return Observable.just(endpoint + page)
                .map(this::getElementsFromUrl)
                .flatMap(Observable::from)
                .map(this::getSerieFromElement);

    }

    private List<Element> getElementsFromUrl(String url){

            try {
                return Jsoup.connect(url)
                        .timeout(5000)
                        .get()
                        .select(BLOCK_SELECTOR);

            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
    }

    private Serie getSerieFromElement(Element element){
        String name = getName(element);
        String codeName = getCodeName(element);
        String imageUrl = getImageUrl(element);

        return new Serie(name, codeName, imageUrl);
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

        Pattern pattern = Pattern.compile("(http.*\\.(jpg|png|jpe)?)");

        Matcher matcher = pattern.matcher(style);

        String imageUrl = "NONE";

        if(matcher.find()){
            imageUrl = matcher.group();

        }

        return imageUrl;

    }

}
