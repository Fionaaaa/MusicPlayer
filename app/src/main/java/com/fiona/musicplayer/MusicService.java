package com.fiona.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {

    public MediaPlayer mediaPlayer;
    ArrayList<Song> songList = new ArrayList<>();     //音乐列表
    boolean isInit = false;       //  音乐是否加载完
    public int currentPosition = 0;                   //当前播放的歌曲
    public boolean isplayed = false;     //播放过歌曲

    public MusicService() {
    }

    /**
     * 获得服务的引用
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    class LocalBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();      //新建一个播放器
        //播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //顺序播放
                if (isplayed) {
                    playNext();
                }
            }
        });

        //启动线程加载音乐列表
        new Thread() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{Media._ID, Media.DATA, Media.TITLE, Media.ARTIST, Media.ALBUM, Media.DURATION,Media.ALBUM_ID}, "is_music != ?", new String[]{"0"}, null);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(0);
                    String data = cursor.getString(1);
                    String title = cursor.getString(2);
                    String artist = cursor.getString(3);
                    String album = cursor.getString(4);
                    long duration = cursor.getLong(5);
                    long albumId=cursor.getLong(6);
                    Song song = new Song(id, data, title, artist, album, duration,albumId);
                    songList.add(song);
                }
                cursor.close();
                isInit = true;

                //加载完毕 发广播通知
                Intent intent = new Intent(App.ACTION_LOAD_MUSIC_LIST);
                Bundle bundle = new Bundle();
                bundle.putSerializable(App.EXTRA_MUSIC_LIST, songList);
                intent.putExtra(App.EXTRA_MUSIC_LIST, bundle);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }.start();
    }

    /**
     * 播放音乐
     *
     * @param position
     */
    public void play(int position) {
        isplayed = true;
        currentPosition = position;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(songList.get(position).data);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        //发本地广播：正在播放的歌曲
        Song song = songList.get(position);
        Intent intent = new Intent(App.ACTION_PLAY_CURRENT);
        intent.putExtra(App.EXTRA_SONG, song);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * 播放/暂停
     */
    public void play() {
        if (isplayed) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            play(currentPosition);  //如果没播放过，则播放第一首
        }
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        if (currentPosition == songList.size() - 1) {
            currentPosition = 0;
        } else {
            currentPosition++;
        }
        play(currentPosition);
    }

    /**
     * 获得播放进度
     *
     * @return
     */
    public int getPlayProgress() {
        return mediaPlayer.getCurrentPosition();
    }
}
