package com.varunbehl.ola.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varunbehl on 16/12/17.
 */
@Dao
public interface MusicDao {
    @Query("SELECT * FROM MusicModel")
    List<MusicModel> getAll();

    @Query("SELECT * FROM MusicModel WHERE id IN (:userIds)")
    List<MusicModel> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM MusicModel WHERE isfav == (1)")
    List<MusicModel> loadMyPlaylist();

    @Insert
    void insert(MusicModel musicModel);

    @Delete
    void delete(MusicModel musicModel);

    @Update
    void update(MusicModel musicModel);



}