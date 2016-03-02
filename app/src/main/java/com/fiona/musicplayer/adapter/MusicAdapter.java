package com.fiona.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiona.musicplayer.R;
import com.fiona.musicplayer.Song;
import com.fiona.musicplayer.TimeUtil;

import java.util.ArrayList;

/**
 * Created by fiona on 15-12-27.
 */
public class MusicAdapter extends BaseAdapter {

    ArrayList<Song> data;
    Context context;
    LayoutInflater layoutInflater;

    public MusicAdapter(Context context, ArrayList<Song> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_music, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //绑定数据
        holder.textViewTitle.setText(data.get(position).title);
        holder.textViewArtist.setText(data.get(position).artist);
        holder.textViewDuration.setText(TimeUtil.formatDuration(data.get(position).duration));
        return convertView;
    }

    class Holder {
        TextView textViewTitle;
        TextView textViewArtist;
        TextView textViewDuration;

        public Holder(View view) {
            textViewArtist = (TextView) view.findViewById(R.id.item_music_artist);
            textViewTitle = (TextView) view.findViewById(R.id.item_music_title);
            textViewDuration = (TextView) view.findViewById(R.id.item_music_duration);
        }
    }
}
