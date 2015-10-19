package com.dexafree.seriescountdown.interactors;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchSuggestionsInteractor {

    private final static String SUGGESTIONS_API_ENDPOINT = "https://www.episodate.com/api/search-suggestions?query=";

    public Subscription loadSuggestions(Observer<List<String>> observer, String query){

        String url = SUGGESTIONS_API_ENDPOINT + Uri.encode(query);

        return Observable.just(url)
                .map(this::getContentFromUrl)
                .map(this::parseResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    private String getContentFromUrl(String urlString){

        try{

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());


            String response = "";

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(in));
            String s;
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> parseResponse(String response){

        List<String> series = new ArrayList<>();

        Gson gson = new GsonBuilder().create();
        JsonArray jsonResponse = gson.fromJson(response, JsonArray.class);

        for(JsonElement element : jsonResponse){
            series.add(element.getAsString());
        }

        return series;
    }

}
