package com.varunbehl.ola.network;

import com.varunbehl.ola.model.AudioFiles;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by varunbehl on 16/12/17.
 */

interface DataInterface {
    @GET("studio")
    Observable<List<AudioFiles>> listAudioInfo();


}
