package com.fiona.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by fiona on 15-12-23.
 */
public class Song implements Serializable {
    public long id;
    public String data;
    public String title;
    public String artist;
    public String album;
    public long duration;
    public long albumId;

    public Song() {
    }

    public Song(long id, String data, String title, String artist, String album, long duration, long albumId) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.albumId = albumId;
    }

    /**
     * 获得专辑图片
     *
     * @param albumId
     * @param context
     * @return
     */
    public Bitmap getAlbumBitmap(long albumId, Context context) {

        String album_uri = "content://media/external/audio/albumart";    //专辑Uri对应的字符串
        Uri albumUri = ContentUris.withAppendedId(Uri.parse(album_uri), albumId);
        // 得到一个输入流
        Bitmap bitmap = null;
        try {
            InputStream is = context.getContentResolver().openInputStream(albumUri);
            if (null != is) {
                bitmap = BitmapFactory.decodeStream(is);
            }
            if(is!=null){
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                '}';
    }
}
