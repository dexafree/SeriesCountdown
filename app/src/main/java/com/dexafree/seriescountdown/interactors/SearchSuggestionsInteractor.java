package com.dexafree.seriescountdown.interactors;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Carlos on 4/9/15.
 */
public class SearchSuggestionsInteractor {

    private final static String SUGGESTIONS_API_ENDPOINT = "http://www.episodate.com/api/search-suggestions?query=";


    public Subscription loadSuggestions(String query, Observer<List<String>> observer){

        String url = SUGGESTIONS_API_ENDPOINT + Uri.encode(query);

        return Observable.just(url)
                .map(this::getContentFromUrl)
                .map(content -> content.replace("\\[", "").replace("\\]", "").replace("\"", ""))
                .map(prepared -> prepared.split(", "))
                .map(Arrays::asList)
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
}
