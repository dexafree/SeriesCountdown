package com.dexafree.seriescountdown.interactors;

import com.dexafree.seriescountdown.model.Serie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Carlos on 17/10/15.
 */
public class GetPopularSeriesInteractor extends BaseSeriesInteractor {

    private final static String POPULAR_SERIES_ENDPOINT = "https://www.episodate.com/api/most-popular?page=";

    private int maxPage = -1;

    public Subscription loadSeries(Observer<Serie> subscriber, int page) {

        if(maxPage == -1 ||  page <= maxPage) {

            String url = POPULAR_SERIES_ENDPOINT + page;

            return getObservable(url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subscriber);
        } else {
            return null;
        }

    }

    private Observable<Serie> getObservable(String endpoint){

        return Observable.just(endpoint)
                .map(this::getUrlContent)
                .map(this::parseResponse)
                .map(this::getSeriesFromResponse)
                .flatMap(Observable::from);

    }

    private String getUrlContent(String endpoint){

        try {
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder content = new StringBuilder();
            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();

            String response = content.toString();
            return response;

        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    private PopularResponse parseResponse(String response){

        Gson gson = new GsonBuilder().create();
        JsonObject object = gson.fromJson(response, JsonObject.class);

        if(object != null){
            PopularResponse responseObject = PopularResponse.fromJson(object);

            if(maxPage == -1){
                maxPage = responseObject.maxPages;
            }

            return responseObject;
        }

        return null;

    }

    private List<Serie> getSeriesFromResponse(PopularResponse response){
        return response.tvShows;
    }

    private static class PopularResponse {

        int page;
        int maxPages;
        List<Serie> tvShows;

        static PopularResponse fromJson(JsonObject object){
            PopularResponse response = new PopularResponse();
            response.maxPages = object.get("pages").getAsInt();
            response.page = object.get("page").getAsInt();
            response.tvShows = new ArrayList<>();

            JsonArray series = object.get("tv_shows").getAsJsonArray();
            for(JsonElement element : series){
                Serie serie = getSerieFromElement(element);
                response.tvShows.add(serie);
            }
            return response;
        }

        private static Serie getSerieFromElement(JsonElement element){
            JsonObject object = element.getAsJsonObject();
            String name = object.get("name").getAsString();
            String codename = object.get("permalink").getAsString();
            String thumbnail = object.get("image_thumbnail_path").getAsString().replace("http://", "https://");
            return new Serie(name, codename, thumbnail);
        }
    }

}
