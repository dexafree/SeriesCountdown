package com.dexafree.seriescountdown.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carlos on 1/9/15.
 */
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
