package com.dexafree.seriescountdown.interactors.model;

import com.dexafree.seriescountdown.model.CountDown;
import com.dexafree.seriescountdown.model.SerieDetail;
import com.dexafree.seriescountdown.utils.ContentUtils;
import com.dexafree.seriescountdown.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SerieDetailDeserializer implements JsonDeserializer<SerieDetail> {

    @Override
    public SerieDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();

        if(object.has("tvShow")) {

            JsonObject root = object.get("tvShow").getAsJsonObject();

            int id = root.get("id").getAsInt();
            String name = extractAttribute(root, "name");
            String codeName = extractAttribute(root, "permalink");
            String description = ContentUtils.cleanHTMLtags(extractAttribute(root, "description"));
            String startDate = extractAttribute(root, "start_date");
            String endDate = extractAttribute(root, "end_date");
            String imageThumbnailPath = extractAttribute(root, "image_thumbnail_path").replace("http:", "https:");
            String imagePath = extractAttribute(root, "image_path").replace("http:", "https:");
            String rating = extractAttribute(root, "rating");
            ArrayList<String> genres = extractGenres(root);
            CountDown countDown = extractCountdown(root);

            return new SerieDetail(id, name, codeName, description, startDate, endDate,
                    imageThumbnailPath, imagePath, rating, genres, countDown);
        }

        return null;
    }

    private String extractAttribute(JsonObject root, String attribute){
        JsonElement element = root.get(attribute);
        if(!element.isJsonNull()){
            return element.getAsString();
        } else {
            return "Unknown";
        }
    }

    private ArrayList<String> extractGenres(JsonObject root){
        JsonArray genresArray = root.get("genres").getAsJsonArray();

        List<String> genres = Utils.map(genresArray, JsonElement::getAsString);

        return new ArrayList<>(genres);
    }

    private CountDown extractCountdown(JsonObject root){

        JsonElement countDownElement = root.get("countdown");

        if(countDownElement.isJsonNull()){
            return null;
        }

        JsonObject countDown = countDownElement.getAsJsonObject();

        int season = countDown.get("season").getAsInt();
        int episode = countDown.get("episode").getAsInt();
        String name = countDown.get("name").getAsString();
        String airDate = countDown.get("air_date").getAsString();

        return new CountDown(season, episode, name, airDate);

    }
}
