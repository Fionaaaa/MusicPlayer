package com.fiona.musicplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fiona.musicplayer.MainActivity;
import com.fiona.musicplayer.R;
import com.fiona.musicplayer.Song;
import com.fiona.musicplayer.adapter.MusicAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    ListView listView;
    MusicAdapter adapter;
    ArrayList<Song> songList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        listView = (ListView) view.findViewById(R.id.listView_music);
        adapter = new MusicAdapter(getActivity(), songList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).play(position);    //点击播放歌曲
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 加载音乐列表
     *
     * @param data
     */
    public void loadMusicList(ArrayList<Song> data) {
        Log.d("debug", "load");
        if (songList.size() < 1) {
            for (Song song : data) {
                songList.add(song);
                Log.d("debug_1", String.valueOf(song.id));
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
