package com.dexafree.seriescountdown.interactors;

import android.util.Log;

import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.utils.ContentUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private final static String SEARCH_SERIES_ENDPOINT = "https://www.episodate.com/search?q=%s&page=";


    private final static String BLOCK_SELECTOR = "div.mix-border > a";
    private final static String IMAGE_SELECTOR = "div.image-block";
    private final static String TITLE_SELECTOR = "span.sorting-cover > span";


    private final static String REDIRECTED_TITLE_SELECTOR = ".breadcrumbs > .container > .pull-left";
    private final static String REDIRECTED_IMAGE_SELECTOR = "img.img-responsive";


    public Subscription searchSeries(Observer<Serie> subscriber, String query, int page){

        String urlWithoutPageIndex = SEARCH_SERIES_ENDPOINT.replace("%s", query).replace(" ", "+") + 1;
        String urlWithPageIndex = SEARCH_SERIES_ENDPOINT.replace("%s", query).replace(" ", "+") + page;

        return Observable.just(urlWithoutPageIndex)
                .flatMap(this::getSearchInfo)
                .flatMap(searchResponse -> {
                    if (searchResponse != null) {
                        if (urlWithoutPageIndex.equalsIgnoreCase(searchResponse.connectedUrl)) {
                            return getObservable(urlWithPageIndex);
                        } else {
                            return getSingleSerie(searchResponse.content, searchResponse.connectedUrl);
                        }
                    } else {
                        return Observable.just(null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    private Observable<SearchResponse> getSearchInfo(String urlString){
        try{

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());

            String connectedUrl = conn.getURL().toString();

            String response = ContentUtils.readContentFromStream(in);


            SearchResponse searchResponse = new SearchResponse(response, connectedUrl);
            return Observable.just(searchResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Observable.just(null);
    }

    private Observable<Serie> getSingleSerie(String content, String connectedUrl){
        Document doc = Jsoup.parse(content);

        String title = doc.select(REDIRECTED_TITLE_SELECTOR).text();
        String[] codeNameSplits = connectedUrl.split("/");
        String codeName = codeNameSplits[codeNameSplits.length-1];

        String imageUrl = doc.select(REDIRECTED_IMAGE_SELECTOR).attr("src").replace("full", "thumbnail");

        Serie serie = new Serie(title, codeName, imageUrl);

        return Observable.just(serie);

    }


    private Observable<Serie> getObservable(String endpoint){

        return Observable.just(endpoint)
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

        String imageUrl = "NONE";

        Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(style);
        if(matcher.find()) {
            imageUrl = matcher.group(1);
        }



        return imageUrl;
    }

    private static class SearchResponse {
        String content;
        String connectedUrl;

        public SearchResponse(String content, String connectedUrl) {
            this.content = content;
            this.connectedUrl = connectedUrl;
        }
    }

}
