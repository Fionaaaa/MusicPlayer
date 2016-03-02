package com.fiona.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiona.musicplayer.Artist;
import com.fiona.musicplayer.R;

import java.util.ArrayList;

/**
 * Created by fiona on 15-12-28.
 */
public class ArtistAdapter extends BaseAdapter {
    ArrayList<Artist> data;
    LayoutInflater inflater;

    public ArtistAdapter(ArrayList<Artist> data, Context context) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_artist, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textViewArtist.setText(data.get(position).name);
        holder.textViewCount.setText(data.get(position).count + "首");
        return convertView;
    }

    class ViewHolder {
        TextView textViewArtist;
        TextView textViewCount;

        ViewHolder(View view) {
            textViewArtist = (TextView) view.findViewById(R.id.textView_artist_artist);
            textViewCount = (TextView) view.findViewById(R.id.textView_count_artist);
        }
    }
}
