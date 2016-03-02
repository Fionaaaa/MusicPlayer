package com.fiona.musicplayer;

import android.graphics.Bitmap;

/**
 * Created by fiona on 15-12-28.
 */
public class Album {
    public String album;
    public int count;
    public Bitmap bitmap;

    public Album(int count, String album) {
        this.album = album;
        this.count = count;
    }

    public Album(int count, String album, Bitmap bitmap) {
        this.album = album;
        this.count = count;
        this.bitmap = bitmap;
    }
}
