package com.dexafree.seriescountdown.interactors;

import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.dexafree.seriescountdown.model.CountDown;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieDetail;
import com.dexafree.seriescountdown.utils.ContentUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fj.data.IterableW;


/**
 * Created by Carlos on 22/9/15.
 */
public class SerieDetailNewApiInteractor extends BaseInteractor<SerieDetail> {

    private final static String API_ENDPOINT = "http://www.episodate.com/api/show-details?query=";

    public interface Callback extends BaseInteractor.Callback<SerieDetail>{}

    public SerieDetailNewApiInteractor(Callback callback) {
        super(callback);
    }

    public void loadSerieDetails(Serie serie){

        final String urlString = API_ENDPOINT + serie.getCodeName();

        AsyncJob.doInBackground(() -> {

            try {

                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String content = ContentUtils.readContentFromStream(connection.getInputStream());

                SerieDetail detail = parseContent(content);
                sendResult(detail);

            } catch (IOException e) {
                e.printStackTrace();
                sendError();
            }

        });

    }

    private SerieDetail parseContent(String content){
        Gson gson = new GsonBuilder().create();

        JsonObject root = gson.fromJson(content, JsonObject.class).get("tvShow").getAsJsonObject();

        int id = root.get("id").getAsInt();
        String name = root.get("name").getAsString();
        String codeName = root.get("permalink").getAsString();
        String description = root.get("description").getAsString();
        String startDate = root.get("start_date").getAsString();
        String endDate = root.get("end_date").getAsString();
        String imageThumbnailPath = root.get("image_thumbnail_path").getAsString();
        String imagePath = root.get("image_path").getAsString();
        String rating = root.get("rating").getAsString();
        List<String> genres = extractGenres(root);
        CountDown countDown = extractCountdown(root);

        return new SerieDetail(id, name, codeName, description, startDate, endDate,
                               imageThumbnailPath, imagePath, rating, genres, countDown);
    }

    private List<String> extractGenres(JsonObject root){
        JsonArray genresArray = root.get("genres").getAsJsonArray();

        IterableW<JsonElement> elements = IterableW.wrap(genresArray);

        List<String> genres = elements.map(JsonElement::getAsString).toStandardList();
        return genres;
    }

    private CountDown extractCountdown(JsonObject root){

        JsonElement countDownElement = root.get("countdown");

        if(countDownElement.isJsonNull()){
            return null;
        }

        JsonObject countDown = countDownElement.getAsJsonObject();

        //Log.d("SERIEDETAILNEWAPIINTERACTOR", "COUNTDOWN: "+countDown.toString());

        int season = countDown.get("season").getAsInt();
        int episode = countDown.get("episode").getAsInt();
        String name = countDown.get("name").getAsString();
        String airDate = countDown.get("air_date").getAsString();

        return new CountDown(season, episode, name, airDate);

    }

}
