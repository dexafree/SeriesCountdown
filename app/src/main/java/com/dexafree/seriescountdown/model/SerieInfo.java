package com.dexafree.seriescountdown.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SerieInfo implements Parcelable {

    private String genres;
    private String status;
    private String start;
    private String end;
    private String remaining;
    private String nextEpisode;
    private String dateNextEpisode;

    public SerieInfo(String genres, String status, String start, String end,
                     String remaining, String nextEpisode, String dateNextEpisode) {
        this.genres = genres;
        this.status = status;
        this.start = start;
        this.end = end;
        this.remaining = remaining;
        this.nextEpisode = nextEpisode;
        this.dateNextEpisode = dateNextEpisode;
    }

    public String getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getRemaining() {
        return remaining;
    }

    public String getNextEpisode() {
        return nextEpisode;
    }

    public String getDateNextEpisode() {
        return dateNextEpisode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.genres);
        dest.writeString(this.status);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.remaining);
        dest.writeString(this.nextEpisode);
        dest.writeString(this.dateNextEpisode);
    }

    protected SerieInfo(Parcel in) {
        this.genres = in.readString();
        this.status = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.remaining = in.readString();
        this.nextEpisode = in.readString();
        this.dateNextEpisode = in.readString();
    }

    public static final Parcelable.Creator<SerieInfo> CREATOR = new Parcelable.Creator<SerieInfo>() {
        public SerieInfo createFromParcel(Parcel source) {
            return new SerieInfo(source);
        }

        public SerieInfo[] newArray(int size) {
            return new SerieInfo[size];
        }
    };
}
