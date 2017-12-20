package com.varunbehl.ola.player;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbehl.ola.R;
import com.varunbehl.ola.Utill.DatabaseUtil;
import com.varunbehl.ola.adapter.AudioFilesAdapter;
import com.varunbehl.ola.database.MusicModel;
import com.varunbehl.ola.model.AudioFiles;
import com.varunbehl.ola.network.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AudioFilesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;
    public List<AudioFiles> audioList;
    private Context context;
    private RecyclerView recyclerView;
    private List<MusicModel> musicModelList;
    private AudioFilesAdapter audioFilesAdapter;


    public AudioFilesFragment() {
    }

    @SuppressWarnings("unused")
    public static AudioFilesFragment newInstance() {
        AudioFilesFragment fragment = new AudioFilesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audiofiles_list, container, false);
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            new MainPageThread(1).start();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void sort(String query) {
        if (query != null) {
            if (audioFilesAdapter == null) {
                if (musicModelList == null) {
                    musicModelList = DatabaseUtil.getInstance(getContext()).musicDao().getAll();
                }
            }
            if (musicModelList != null) {
                audioFilesAdapter = new AudioFilesAdapter(getContext(), musicModelList, mListener);
                audioFilesAdapter.filter(query);

            }
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MusicModel item);

        void onAddingSongToPlayList(MusicModel item);

        void onDownloading(MusicModel item);
    }


    private class MainPageThread extends Thread {
        final int requestType;

        MainPageThread(int requestType) {
            this.requestType = requestType;
        }

        @Override
        public void run() {
            super.run();

            Observable<List<AudioFiles>> topRatedObservable = RetrofitManager.getInstance().listAudio();

            musicModelList = DatabaseUtil.getInstance(getContext()).musicDao().getAll();

            if (musicModelList == null || musicModelList.isEmpty()) {

                topRatedObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<List<AudioFiles>>() {
                                       @Override
                                       public void onCompleted() {
                                           musicModelList = new ArrayList<>();
                                           for (AudioFiles audioFiles : audioList) {
                                               MusicModel musicModel = new MusicModel();
                                               musicModel.setArtists(audioFiles.getArtists());
                                               musicModel.setCoverImage(audioFiles.getCoverImage());
                                               musicModel.setSong(audioFiles.getSong());
                                               musicModel.setUrl(audioFiles.getUrl());
                                               musicModelList.add(musicModel);
                                               DatabaseUtil.getInstance(getContext()).musicDao().insert(musicModel);
                                           }
                                           recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                           audioFilesAdapter = new AudioFilesAdapter(getContext(), musicModelList, mListener);

                                           recyclerView.setAdapter(audioFilesAdapter);
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           e.printStackTrace();
                                           Log.v("Exception", "NullPointerException");
                                       }

                                       @Override
                                       public void onNext(List<AudioFiles> audioFiles) {
                                           audioList = audioFiles;
                                       }
                                   }
                        );
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        audioFilesAdapter = new AudioFilesAdapter(getContext(), musicModelList, mListener);
                        recyclerView.setAdapter(audioFilesAdapter);
                    }//public void run() {
                });


            }
        }

    }
}
