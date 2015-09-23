package com.dexafree.seriescountdown.model;

import java.util.List;

/**
 * Created by Carlos on 22/9/15.
 */
public class SerieDetail {

    private int id;
    private String name;
    private String codeName;
    private String description;
    private String startDate;
    private String endDate;
    private String imageThumbnailPath;
    private String imagePath;
    private String rating;
    private List<String> genres;
    private CountDown countDown;

    public SerieDetail(int id, String name, String codeName, String description, String startDate,
                       String endDate, String imageThumbnailPath, String imagePath, String rating,
                       List<String> genres, CountDown countDown) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageThumbnailPath = imageThumbnailPath;
        this.imagePath = imagePath;
        this.rating = rating;
        this.genres = genres;
        this.countDown = countDown;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate.equals("") ? "Unknown" : endDate;
    }

    public String getImageThumbnailPath() {
        return imageThumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getRating() {
        return rating;
    }

    public String getGenres() {
        StringBuilder genresString = new StringBuilder("");

        if(this.genres != null){
            for(String genre : this.genres){
                if(!genresString.toString().equals("")) {
                    genresString.append(", ");
                }
                genresString.append(genre);
            }
        }

        return genresString.toString();
    }


    public int getSeason() {
        return countDown == null ? 0 : countDown.getSeason();
    }

    public int getEpisode() {
        return countDown == null ? 0 : countDown.getEpisode();
    }

    public String getEpisodeName() {
        return countDown == null ? "Unknown" : countDown.getEpisodeName();
    }

    public String getAirDate() {
        return countDown == null ? "Unknown" : countDown.getAirDate();
    }

    public boolean hasCountdown(){
        return countDown == null;
    }
}
