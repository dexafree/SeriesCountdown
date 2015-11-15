package com.dexafree.seriescountdown.interactors.model;

import com.dexafree.seriescountdown.model.Serie;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 16/11/15.
 */
public class SearchResponse {

    @SerializedName("page")
    int page;

    @SerializedName("pages")
    int maxPages;

    @SerializedName("tv_shows")
    List<Serie> tvShows;

    public int getPage() {
        return page;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public List<Serie> getTvShows() {
        return tvShows;
    }

    @Override
    public String toString() {
        return "PAGE: "+page+" | MAXPAGES: "+maxPages+" | TVSHOWS:" +tvShows.toString();
    }
}
