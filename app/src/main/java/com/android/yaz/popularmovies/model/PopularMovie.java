package com.android.yaz.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "movie")
public class PopularMovie implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "title")
    private String originalTitle;
    @ColumnInfo(name = "poster")
    private String posterPath;
    private String synopsis;
    @ColumnInfo(name = "user_rating")
    private String userRating;
    @ColumnInfo(name = "released_date")
    private String releasedDate;

    public PopularMovie(String id, String originalTitle, String posterPath, String synopsis, String userRating, String releasedDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releasedDate = releasedDate;
    }

    @Ignore
    protected PopularMovie(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        synopsis = in.readString();
        userRating = in.readString();
        releasedDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(synopsis);
        parcel.writeString(userRating);
        parcel.writeString(releasedDate);
    }

    public static final Creator<PopularMovie> CREATOR = new Creator<PopularMovie>() {
        @Override
        public PopularMovie createFromParcel(Parcel in) {
            return new PopularMovie(in);
        }

        @Override
        public PopularMovie[] newArray(int size) {
            return new PopularMovie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }
}

