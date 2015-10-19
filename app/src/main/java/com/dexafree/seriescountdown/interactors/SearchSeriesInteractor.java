package com.dexafree.seriescountdown.interactors;


import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.utils.ContentUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchSeriesInteractor extends BaseSeriesInteractor {

    private final static String SEARCH_SERIES_ENDPOINT = "https://www.episodate.com/api/search?q=%s&page=";

    private String lastQuery;
    private int maxPages = -1;


    public Subscription searchSeries(Observer<Serie> subscriber, String query, int page){


        if(!query.equalsIgnoreCase(lastQuery)
                || maxPages == -1
                || (query.equalsIgnoreCase(lastQuery) && page < maxPages)) {

            this.lastQuery = query;

            String urlWithPageIndex = SEARCH_SERIES_ENDPOINT.replace("%s", query).replaceAll(" ", "+") + page;

            return Observable.just(urlWithPageIndex)
                    .map(this::getResponseContent)
                    .map(response -> new GsonBuilder().create().fromJson(response, JsonObject.class))
                    .map(SearchResponse::fromJson)
                    .map(response -> {
                        maxPages = response.pages;
                        return response.tvShows;
                    })
                    .flatMap(Observable::from)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subscriber);
        }

        return null;

    }

    private String getResponseContent(String urlString){

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());

            return ContentUtils.readContentFromStream(in);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }


    private static class SearchResponse {
        int page;
        int pages;
        List<Serie> tvShows;

        static SearchResponse fromJson(JsonObject object){

            SearchResponse response = new SearchResponse();

            response.page = object.get("page").getAsInt();
            response.pages = object.get("pages").getAsInt();
            response.tvShows = new ArrayList<>();

            JsonArray tvShows = object.get("tv_shows").getAsJsonArray();
            for(JsonElement element : tvShows){
                Serie serie = parseElement(element);
                response.tvShows.add(serie);
            }

            return response;
        }

        private static Serie parseElement(JsonElement element){
            JsonObject object = element.getAsJsonObject();
            String name = object.get("name").getAsString();
            String codename = object.get("permalink").getAsString();
            String thumbnail = object.get("image_thumbnail_path").getAsString().replace("http:", "https:");

            if(thumbnail.equalsIgnoreCase("https://www.episodate.com")){
                thumbnail = "http://i.imgur.com/443ZL8t.jpg";
            }

            return new Serie(name, codename, thumbnail);
        }
    }

}
