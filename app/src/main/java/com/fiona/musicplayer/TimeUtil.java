package com.fiona.musicplayer;

/**
 * Created by fiona on 15-12-24.
 */
public class TimeUtil {

    /**
     * 格式化歌曲持续时间
     *
     * @param duration
     * @return
     */
    public static String formatDuration(long duration) {
        long t = duration / 1000;
        int m = (int) (t / 60);
        int s = (int) (t % 60);
        return String.format("%2d:%02d", m, s);
    }
}
