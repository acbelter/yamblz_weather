package me.maximpestryakov.yamblzweather.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "place", indices = {@Index("place_id")})
public class PlaceData {
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
