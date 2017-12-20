package com.varunbehl.ola.player;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbehl.ola.R;
import com.varunbehl.ola.Utill.DatabaseUtil;
import com.varunbehl.ola.database.MusicModel;
import com.varunbehl.ola.model.AudioFiles;

import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyPlaylistFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public List<AudioFiles> audioList;
    private Context context;
    private RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;


    public MyPlaylistFragment() {
    }

    @SuppressWarnings("unused")
    public static MyPlaylistFragment newInstance(boolean isRecent) {
        MyPlaylistFragment fragment = new MyPlaylistFragment();
        Bundle args = new Bundle();
        args.putBoolean("isRecent", isRecent);
        fragment.setArguments(args);
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
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (getArguments().getBoolean("isRecent")) {
                int[] recentIds = DatabaseUtil.getInstance(getContext()).recentMusicDao().loadRecentSongsId();
                List<MusicModel> musicModelList = DatabaseUtil.getInstance(getContext()).musicDao().loadAllByIds(recentIds);
                recyclerView.setAdapter(new com.varunbehl.ola.adapter.MyPlaylistFragment(getContext(), musicModelList, mListener));
            } else {
                recyclerView.setAdapter(new com.varunbehl.ola.adapter.MyPlaylistFragment(getContext(), DatabaseUtil.getInstance(getContext()).musicDao().loadMyPlaylist(), mListener));
            }
        }
        return view;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MusicModel item);

        void onAddingSongToPlayList(MusicModel item);
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


}
