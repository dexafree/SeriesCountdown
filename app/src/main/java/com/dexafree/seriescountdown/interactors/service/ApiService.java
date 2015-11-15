package com.dexafree.seriescountdown.interactors.service;


import com.dexafree.seriescountdown.interactors.model.SearchResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {

    @GET("/most-popular")
    Observable<SearchResponse> getPopularSeries(@Query("page") Integer page);

    @GET("/search")
    Observable<SearchResponse> searchSerie(@Query("q") String serie, @Query("page") Integer page);

    @GET("/search-suggestions")
    Observable<List<String>> getSuggestions(@Query("query") String query);

}
