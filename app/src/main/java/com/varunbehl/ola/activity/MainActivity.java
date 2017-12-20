package com.varunbehl.ola.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.varunbehl.ola.R;
import com.varunbehl.ola.Utill.DatabaseUtil;
import com.varunbehl.ola.database.MusicModel;
import com.varunbehl.ola.database.RecentMusicModel;
import com.varunbehl.ola.player.AudioFilesFragment;
import com.varunbehl.ola.player.MyPlaylistFragment;

public class MainActivity extends AppCompatActivity implements AudioFilesFragment.OnListFragmentInteractionListener, MyPlaylistFragment.OnListFragmentInteractionListener {

    android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
    private MusicModel musicFile;

    private AudioFilesFragment audioFilesFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                     audioFilesFragment = new AudioFilesFragment();
                    fragmentManager1.beginTransaction()
                            .replace(R.id.framelayout, AudioFilesFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentManager1.beginTransaction()
                            .replace(R.id.framelayout, MyPlaylistFragment.newInstance(false)).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentManager1.beginTransaction()
                            .replace(R.id.framelayout, MyPlaylistFragment.newInstance(true)).commit();
                    return true;
            }
            return false;
        }
    };
    private boolean isLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (audioFilesFragment!=null){
                    audioFilesFragment.sort(query);
                }
//                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
//                intent.putExtra("search", query);
//                startActivity(intent);
                return true;
            }
        };


        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }


    @Override
    public void onDownloading(MusicModel item) {
        isLocal = chceckIfFileExists(item);
        if (!isLocal) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(item);
                } else {
                    this.musicFile = item;
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                //permission is automatically granted on sdk<23 upon installation
            }
        }

    }

    private boolean chceckIfFileExists(MusicModel item) {
        String filePath = String.valueOf(Environment.getExternalStorageDirectory()) + "/Songs" + "/" + item.getSong() + ".mp3";

        DataSpec dataSpec = new DataSpec(Uri.parse(filePath));
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
            return true;
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadFile(musicFile);
        }
    }


    public void downloadFile(MusicModel item) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.getUrl()));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("Downloading " + item.getSong());
            request.setDescription("By " + item.getArtists());
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(String.valueOf(Environment.getExternalStorageDirectory()), "/Songs" + "/" + item.getSong() + ".mp3");

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            long refid = downloadManager.enqueue(request);

            Log.e("OUT", "" + refid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAddingSongToPlayList(MusicModel musicModel) {
        musicModel.setIsfav(true);
        DatabaseUtil.getInstance(this).musicDao().update(musicModel);
    }

    @Override
    public void onListFragmentInteraction(MusicModel item) {
        RecentMusicModel recentMusicModel = new RecentMusicModel();
        recentMusicModel.setTimeInMillisec(System.currentTimeMillis());
        recentMusicModel.setMusicModelId(item.getId());
        DatabaseUtil.getInstance(this).recentMusicDao().addRecent(recentMusicModel);

        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("URL", item.getUrl());
        intent.putExtra("isLocal", isLocal);
        startActivity(intent);

    }
}
