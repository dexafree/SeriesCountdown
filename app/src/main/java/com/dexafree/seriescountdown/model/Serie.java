package com.dexafree.seriescountdown.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Serie implements Parcelable {

    @SerializedName("name")
    String name;

    @SerializedName("permalink")
    String codeName;

    @SerializedName("image_thumbnail_path")
    String imageUrl;

    public Serie(String name, String codeName, String imageUrl) {
        this.name = name;
        this.codeName = codeName;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getImageUrl() {
        String tempUrl = imageUrl.replace("http:", "https:");
        if(tempUrl.equalsIgnoreCase("https://www.episodate.com")){
            return "http://i.imgur.com/443ZL8t.jpg";
        }
        return tempUrl;
    }

    public String getImageHDUrl() {
        return getImageUrl().replace("thumbnail", "full");
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Serie) {

            Serie newSerie = (Serie) o;

            return name.equals(newSerie.name) && codeName.equals(newSerie.codeName) && imageUrl.equals(newSerie.imageUrl);

        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.codeName);
        dest.writeString(this.imageUrl);
    }

    protected Serie(Parcel in) {
        this.name = in.readString();
        this.codeName = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Serie> CREATOR = new Parcelable.Creator<Serie>() {
        public Serie createFromParcel(Parcel source) {
            return new Serie(source);
        }

        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    @Override
    public String toString() {
        return "NAME: "+name+" | CODENAME: "+codeName+" | URL: "+getImageUrl();
    }
}

