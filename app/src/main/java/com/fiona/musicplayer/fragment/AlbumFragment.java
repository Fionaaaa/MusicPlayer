package com.fiona.musicplayer.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fiona.musicplayer.Album;
import com.fiona.musicplayer.R;
import com.fiona.musicplayer.Song;
import com.fiona.musicplayer.adapter.AlbumAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {
    ListView listView;
    AlbumAdapter adapter;
    ArrayList<Album> data = new ArrayList<>();

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        listView = (ListView) view.findViewById(R.id.listView_album);
        adapter = new AlbumAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * 加载专辑列表
     *
     * @param songList
     */
    public void loadAlbumList(ArrayList<Song> songList, Context context1) {
        final ArrayList<Song> list = songList;
        final Context context = context1;
        new Thread() {
            @Override
            public void run() {
                if (data.size() < 1) {
                    for (Song song : list) {
                        Bitmap bitmap = song.getAlbumBitmap(song.albumId, context);
                        if (contain(song)) {
                            data.get(containPosition(song)).count++;
                            if (bitmap != null) {
                                data.get(containPosition(song)).bitmap = bitmap;
                                Log.d("debug", "新建album3");
                            }
                        } else {
                            if (bitmap == null) {
                                data.add(new Album(1, song.album));
                                Log.d("debug", "新建album1");
                            } else {
                                data.add(new Album(1, song.album, bitmap));
                                Log.d("debug", "新建album2");
                            }
                        }
                    }
                    while (true) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }.start();

    }

    /**
     * 判断是否已存在此专辑
     *
     * @param song
     * @return
     */
    private boolean contain(Song song) {
        for (Album a : data) {
            if (a.album.equals(song.album)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回所包含专辑的位置
     *
     * @param song
     * @return
     */
    private int containPosition(Song song) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).album.equals(song.album)) {
                return i;
            }
        }
        return -1;
    }

}
