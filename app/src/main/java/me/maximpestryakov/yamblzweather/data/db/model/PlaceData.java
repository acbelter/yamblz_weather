package me.maximpestryakov.yamblzweather.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "place", indices = {@Index("place_id")})
public class PlaceData implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "place_id")
    public String placeId;
    @ColumnInfo(name = "lat")
    public float lat;
    @ColumnInfo(name = "lng")
    public float lng;
    @ColumnInfo(name = "place_name")
    public String placeName;
    @ColumnInfo(name = "lang")
    public String lang;

    public PlaceData() {
    }

    @Ignore
    public PlaceData(String placeId) {
        this.placeId = placeId;
    }

    protected PlaceData(Parcel in) {
        placeId = in.readString();
        lat = in.readFloat();
        lng = in.readFloat();
        placeName = in.readString();
        lang = in.readString();
    }

    public static final Creator<PlaceData> CREATOR = new Creator<PlaceData>() {
        @Override
        public PlaceData createFromParcel(Parcel in) {
            return new PlaceData(in);
        }

        @Override
        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaceData placeData = (PlaceData) o;
        return placeId.equals(placeData.placeId);
    }

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeId);
        parcel.writeFloat(lat);
        parcel.writeFloat(lng);
        parcel.writeString(placeName);
        parcel.writeString(lang);
    }

    @Override
    public String toString() {
        return "PlaceData{" +
                "placeId='" + placeId + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", placeName='" + placeName + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
