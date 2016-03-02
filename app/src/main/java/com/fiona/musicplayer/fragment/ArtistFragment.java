package com.fiona.musicplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fiona.musicplayer.Artist;
import com.fiona.musicplayer.R;
import com.fiona.musicplayer.Song;
import com.fiona.musicplayer.adapter.ArtistAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 */
public class ArtistFragment extends Fragment {

    ListView listView;
    ArtistAdapter adapter;
    ArrayList<Artist> data = new ArrayList<>();

    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        listView = (ListView) view.findViewById(R.id.listView_artist);
        adapter = new ArtistAdapter(data, getActivity());
        listView.setAdapter(adapter);

        return view;
    }

    /**
     * 加载歌手列表
     *
     * @param list
     */
    public void loadArtistList(ArrayList<Song> list) {
        if (data.size() < 1) {
            for (Song song : list) {
                if (contain(song)) {
                    data.get(containPosition(song)).count++;
                } else {
                    data.add(new Artist(1, song.artist));
                }
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 判断是否已存在此歌手
     *
     * @param song
     * @return
     */
    private boolean contain(Song song) {
        for (Artist artist : data) {
            if (artist.name.equals(song.artist)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回所包含歌手的位置
     *
     * @param song
     * @return
     */
    private int containPosition(Song song) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).name.equals(song.artist)) {
                return i;
            }
        }
        return -1;
    }
}
