package com.dexafree.seriescountdown.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.dexafree.seriescountdown.model.SerieDetail;

public class DetailPersistance extends PersistableObject implements Parcelable {

    private SerieDetail detail;

    public DetailPersistance(SerieDetail detail) {
        this.detail = detail;
    }

    public SerieDetail getDetail() {
        return detail;
    }

    protected DetailPersistance(Parcel in) {
        detail = in.readParcelable(SerieDetail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(detail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailPersistance> CREATOR = new Creator<DetailPersistance>() {
        @Override
        public DetailPersistance createFromParcel(Parcel in) {
            return new DetailPersistance(in);
        }

        @Override
        public DetailPersistance[] newArray(int size) {
            return new DetailPersistance[size];
        }
    };

}
