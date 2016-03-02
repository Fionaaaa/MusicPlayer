package com.fiona.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fiona.musicplayer.fragment.ArtistFragment;
import com.fiona.musicplayer.fragment.MusicFragment;
import com.fiona.musicplayer.fragment.PlayFragment;
import com.fiona.musicplayer.fragment.AlbumFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tableLayout;
    ViewPager viewPager;
    MusicFragment musicFragment;
    ArtistFragment artistFragment;
    AlbumFragment albumFragment;
    public PlayFragment playFragment;

    public MusicService musicService;    // 音乐服务
    public ArrayList<Song> songList;     // 歌曲列表
    public boolean isReceive = false;    // 是否接已经初始化songList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playFragment = (PlayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_play);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //绑定服务
        bindService(new Intent(this, MusicService.class), conn, BIND_AUTO_CREATE);

        //注册本地广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.ACTION_LOAD_MUSIC_LIST);
        filter.addAction(App.ACTION_PLAY_CURRENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除服务
        unbindService(conn);
        //注销广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    /**
     * ViewPager的适配器
     */
    class PagerAdapter extends FragmentPagerAdapter {
        private String[] title = getResources().getStringArray(R.array.music);
        private ArrayList<Fragment> data = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            musicFragment = new MusicFragment();
            artistFragment = new ArtistFragment();
            albumFragment = new AlbumFragment();
            data.add(musicFragment);
            data.add(artistFragment);
            data.add(albumFragment);
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    /**
     * 服务连接
     */
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getMusicService();
            if (musicService.isInit) {
                songList = musicService.songList;
                isReceive = true;
                musicFragment.loadMusicList(songList);    // 加载音乐列表
                artistFragment.loadArtistList(songList);  // 加载歌手列表
                albumFragment.loadAlbumList(songList, getApplicationContext());    // 加载专辑列表
                Log.d("debug", "从服务获得数据");

                //重进活动，初始化播放状态状态
                Song song = songList.get(musicService.currentPosition);
                playFragment.reComeIn(song);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /**
     * 广播接收器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case App.ACTION_LOAD_MUSIC_LIST:
                    if (!isReceive && musicService != null) {
                        Bundle bundle = intent.getBundleExtra(App.EXTRA_MUSIC_LIST);
                        songList = (ArrayList<Song>) bundle.get(App.EXTRA_MUSIC_LIST);
                        isReceive = true;
                        musicFragment.loadMusicList(songList);      // 加载音乐列表
                        artistFragment.loadArtistList(songList);  // 加载歌手列表
                        albumFragment.loadAlbumList(songList, getApplicationContext());      //加载专辑列表
                        Log.d("debug", "收到广播");
                    }
                    break;
                case App.ACTION_PLAY_CURRENT:
                    Song song = (Song) intent.getSerializableExtra(App.EXTRA_SONG);
                    playFragment.setSongChange(song);
                    break;
            }
        }
    };

    /**
     * 播放歌曲
     *
     * @param position
     */
    public void play(int position) {
        musicService.play(position);

        //播放图标变化
        playFragment.isPause = false;
        playFragment.buttonPlay.setImageResource(R.drawable.ic_pause_white_36dp);
    }

    /**
     * 播放/暂停
     */
    public void playOrPause() {
        musicService.play();
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        musicService.playNext();
    }
}
