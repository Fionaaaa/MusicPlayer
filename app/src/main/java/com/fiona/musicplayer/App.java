package com.fiona.musicplayer;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * 在清单文件中给application 加上name
 * Created by fiona on 15-12-23.
 */
public class App extends Application {

    /**
     * 广播：播放某一首音乐
     */
    public static final String ACTION_PLAY_CURRENT = "com.newer.music.ACTION_PLAY_CURRENT";

    public static final String ACTION_LOAD_MUSIC_LIST = "com.fiona.music.ACTION.LOAD_MUSIC_LIST";

    /**
     * 当前曲目
     */
    public static final String EXTRA_SONG = "song";

    public static final String EXTRA_MUSIC_LIST = "com.fiona.music.EXTRA_MUSIC_LIST";

    /**
     * 通知的动作
     */
    public static final String ACTION_PLAY = "com.fiona.music.ACTION_PLAY";
    public static final String ACTION_PLAY_NEXT = "com.fiona.music.ACTION_PLAY_NEXT";
    public static final String ACTION_PLAY_LAST = "com.fiona.music.ACTION_PLAY_LAST";


    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "程序开始");
        startService(new Intent(this, MusicService.class));
    }
}
