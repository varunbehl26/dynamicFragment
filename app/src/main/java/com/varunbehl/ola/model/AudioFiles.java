package com.varunbehl.ola.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by varunbehl on 16/12/17.
 */

public class AudioFiles implements Parcelable {

    private String song;
    private String url;
    private String artists;

    @SerializedName("cover_image")
    @Expose
    private String coverImage;

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.song);
        dest.writeString(this.url);
        dest.writeString(this.artists);
        dest.writeString(this.coverImage);
    }

    public AudioFiles() {
    }

    protected AudioFiles(Parcel in) {
        this.song = in.readString();
        this.url = in.readString();
        this.artists = in.readString();
        this.coverImage = in.readString();
    }

    public static final Parcelable.Creator<AudioFiles> CREATOR = new Parcelable.Creator<AudioFiles>() {
        @Override
        public AudioFiles createFromParcel(Parcel source) {
            return new AudioFiles(source);
        }

        @Override
        public AudioFiles[] newArray(int size) {
            return new AudioFiles[size];
        }
    };
}