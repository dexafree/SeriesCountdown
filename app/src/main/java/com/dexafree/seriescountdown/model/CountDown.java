package com.dexafree.seriescountdown.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carlos on 22/9/15.
 */
public class CountDown implements Parcelable {

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

    protected CountDown(Parcel in) {
        season = in.readInt();
        episode = in.readInt();
        episodeName = in.readString();
        airDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(season);
        dest.writeInt(episode);
        dest.writeString(episodeName);
        dest.writeString(airDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CountDown> CREATOR = new Creator<CountDown>() {
        @Override
        public CountDown createFromParcel(Parcel in) {
            return new CountDown(in);
        }

        @Override
        public CountDown[] newArray(int size) {
            return new CountDown[size];
        }
    };

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

    @Override
    public String toString() {
        return "["+season+", "+episode+", \""+episodeName+"\", "+airDate+"]";
    }
}
