package com.varunbehl.ola.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by varunbehl on 16/12/17.
 */

@Entity(tableName = "RecentMusicModel")
public class RecentMusicModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recentId")
    int recentId;


    @ColumnInfo(name = "musicModelId")
    int musicModelId;


    @ColumnInfo(name = "timeInMillisec")
    public Long timeInMillisec;


    public int getId() {
        return recentId;
    }

    public void setId(int id) {
        this.recentId = id;
    }

    public int getMusicModelId() {
        return musicModelId;
    }

    public void setMusicModelId(int musicModelId) {
        this.musicModelId = musicModelId;
    }

    public Long getTimeInMillisec() {
        return timeInMillisec;
    }

    public void setTimeInMillisec(Long timeInMillisec) {
        this.timeInMillisec = timeInMillisec;
    }
}