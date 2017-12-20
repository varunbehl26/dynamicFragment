package com.varunbehl.ola.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by varunbehl on 16/12/17.
 */
@Dao
public interface RecentMusicDao {

    @Insert
    void addRecent(RecentMusicModel recentMusicModel);

    @Query("SELECT musicModelId FROM RecentMusicModel ORDER BY recentId desc")
    int[] loadRecentSongsId();

}