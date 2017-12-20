package com.varunbehl.ola.Utill;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.varunbehl.ola.database.AppDatabase;
import com.varunbehl.ola.database.MusicModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by varunbehl on 16/12/17.
 */

public class DatabaseUtil {

    public static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "MusicDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

}
