package com.varunbehl.ola.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.varunbehl.ola.R;
import com.varunbehl.ola.database.MusicModel;

import java.util.List;

public class MyPlaylistFragment extends RecyclerView.Adapter<MyPlaylistFragment.ViewHolder> {

    private final List<MusicModel> mValues;
    private final com.varunbehl.ola.player.MyPlaylistFragment.OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public MyPlaylistFragment(Context context, List<MusicModel> items, com.varunbehl.ola.player.MyPlaylistFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_audiofiles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSongText.setText(mValues.get(position).getSong());
        holder.mArtistText.setText(mValues.get(position).getArtists());

        if (mValues.get(position).isfav!=null && mValues.get(position).isfav){
            holder.mAddImage.setVisibility(View.GONE);
        }
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(new OkHttpDownloader(mContext));
        builder.build()
                .load(mValues.get(position)
                        .getCoverImage())
                        .into(holder.mImageView);

        holder.mAddImage.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onAddingSongToPlayList(holder.mItem);
                }
            }
        }));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSongText;
        public final TextView mArtistText;
        public final ImageView mImageView, mAddImage;
        public MusicModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.music_img);
            mAddImage = (ImageView) view.findViewById(R.id.add_img);
            mSongText = (TextView) view.findViewById(R.id.song_text);
            mArtistText = (TextView) view.findViewById(R.id.artist_text);
        }

    }
}
