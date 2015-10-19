package com.dexafree.seriescountdown.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Serie implements Parcelable {

    private String name;
    private String codeName;
    private String imageUrl;

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
        return imageUrl;
    }

    public String getImageHDUrl(){
        return imageUrl.replace("thumbnail", "full");
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Serie){

            Serie newSerie = (Serie)o;

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
}
