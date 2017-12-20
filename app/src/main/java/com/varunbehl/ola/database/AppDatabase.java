package com.varunbehl.ola.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by varunbehl on 16/12/17.
 */
@Database(entities = {MusicModel.class,RecentMusicModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MusicDao musicDao();
    public abstract RecentMusicDao recentMusicDao();
}
