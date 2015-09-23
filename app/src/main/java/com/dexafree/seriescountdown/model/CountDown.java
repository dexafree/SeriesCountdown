package com.dexafree.seriescountdown.model;

/**
 * Created by Carlos on 22/9/15.
 */
public class CountDown {

    private int season;
    private int episode;
    private String episodeName;
    private String airDate;

    public CountDown(int season, int episode, String episodeName, String airDate) {
        this.season = season;
        this.episode = episode;
        this.episodeName = episodeName;
        this.airDate = airDate;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getAirDate() {
        return airDate;
    }
}
