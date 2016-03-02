package com.fiona.musicplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fiona.musicplayer.MainActivity;
import com.fiona.musicplayer.R;
import com.fiona.musicplayer.Song;

/**
 * 播放控制台
 */
public class PlayFragment extends Fragment {

    public ImageButton buttonPlay;
    ImageButton buttonNext;
    TextView textViewTitle;
    TextView textViewArtist;
    ProgressBar progressBar;
    PlayThread playThread;
    ImageView imageView;

    public boolean isPause = true;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView_play_fragment);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_play);
        textViewTitle = (TextView) view.findViewById(R.id.textView_title_play);
        textViewArtist = (TextView) view.findViewById(R.id.textView_artist_play);
        buttonPlay = (ImageButton) view.findViewById(R.id.imageButton_play);
        buttonNext = (ImageButton) view.findViewById(R.id.imageButton_next);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).playOrPause();
                //点击播放键
                isPause = !isPause;
                if (isPause) {
                    buttonPlay.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    buttonPlay.setImageResource(R.drawable.ic_pause_white_36dp);
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).playNext();
                //点击下一首
                buttonPlay.setImageResource(R.drawable.ic_pause_white_36dp);
                isPause = false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        playThread = new PlayThread();
        playThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        playThread.isFragmentOpen = false;
    }

    /**
     * 播放线程
     */
    class PlayThread extends Thread {
        MainActivity context = (MainActivity) getActivity();
        boolean isFragmentOpen = true;

        @Override
        public void run() {
            while (isFragmentOpen) {
                if (context.musicService != null && context.songList != null) {
                    if (context.musicService.isplayed) {
                        final int progress = context.musicService.getPlayProgress();
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                context.playFragment.progressBar.setProgress(progress);
                            }
                        });
                    } else {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewTitle.setText(context.songList.get(0).title);
                                textViewArtist.setText(context.songList.get(0).artist);
                            }
                        });
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 播放歌曲改变
     *
     * @param song
     */
    public void setSongChange(Song song) {
        textViewTitle.setText(song.title);
        textViewArtist.setText(song.artist);
        progressBar.setMax((int) song.duration);
        imageView.setImageBitmap(song.getAlbumBitmap(song.albumId, getActivity()));
    }

    /**
     * 返回活动 初始化播放状态
     *
     * @param song
     */
    public void reComeIn(Song song) {
        textViewTitle.setText(song.title);
        textViewArtist.setText(song.artist);
        progressBar.setMax((int) song.duration);
        MainActivity context = (MainActivity) getActivity();
        imageView.setImageBitmap(song.getAlbumBitmap(song.albumId, context));
        if (context.musicService.mediaPlayer.isPlaying()) {
            buttonPlay.setImageResource(R.drawable.ic_pause_white_36dp);
            isPause = false;
        } else {
            buttonPlay.setImageResource(R.drawable.ic_play_arrow_white_36dp);
            isPause = true;
        }
    }

}
