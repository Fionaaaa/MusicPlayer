package com.fiona.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiona.musicplayer.Album;
import com.fiona.musicplayer.R;

import java.util.ArrayList;

/**
 * Created by fiona on 15-12-28.
 */
public class AlbumAdapter extends BaseAdapter {
    ArrayList<Album> data;
    LayoutInflater inflater;

    public AlbumAdapter(Context context, ArrayList<Album> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_album, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textViewAlbum.setText(data.get(position).album);
        holder.textViewCount.setText(data.get(position).count + "首");
        if(data.get(position).bitmap!=null) {
            holder.imageView.setImageBitmap(data.get(position).bitmap);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_album_black_36dp);
        }
        return convertView;
    }

    class ViewHolder {
        TextView textViewAlbum;
        TextView textViewCount;
        ImageView imageView;

        ViewHolder(View view) {
            textViewAlbum = (TextView) view.findViewById(R.id.textView_album_album);
            textViewCount = (TextView) view.findViewById(R.id.textView_count_album);
            imageView = (ImageView) view.findViewById(R.id.imageView_album_fragment);
        }
    }
}
