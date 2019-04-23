package test.com.zh.dragcontentlayout.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;


import java.io.File;

import test.com.zh.dragcontentlayout.config.Global;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 媒体播放工具
 */
public class MediaUtil {

    private static final String TAG = "MediaUtil";

    private IMediaPlayer player;
    private EventListener eventListener;
    private String currentPlayFile = null;
    private AudioManager audioManager = null;
    private AudioManager.OnAudioFocusChangeListener changeListener = null;
    private OnStartListener onStartListener;

    private MediaUtil() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        player = new IjkMediaPlayer();
        audioManager = (AudioManager) Global.application.getSystemService(Context.AUDIO_SERVICE);
        changeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

            }
        };
    }

    private static MediaUtil instance = new MediaUtil();

    public static MediaUtil getInstance() {
        return instance;
    }

    public IMediaPlayer getPlayer() {
        return player;
    }

    public void setEventListener(final EventListener eventListener) {
        this.eventListener = eventListener;
        if (player != null) {
            player.setOnCompletionListener(mp -> {
                if (this.eventListener != null) {
                    this.eventListener.onStop();
                    this.eventListener = null;
                }
                currentPlayFile = null;
                try {
                    audioManager.abandonAudioFocus(changeListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void play(File file) {
        try {
            if (eventListener != null) {
                eventListener.onStop();
            }
            try {
                audioManager.requestAudioFocus(changeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
            }
            player = new IjkMediaPlayer();
            currentPlayFile = file.getAbsolutePath();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(currentPlayFile);
            player.prepareAsync();
            player.setVolume(1, 1);
            player.start();
            if (onStartListener != null) {
                onStartListener.onStart();
            }
        } catch (Exception e) {

        }
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
        if (eventListener != null) {
            eventListener.onStop();
            eventListener = null;
        }
        try {
            audioManager.abandonAudioFocus(changeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentPlayFile = null;
    }

    public long getDuration(String path) {
        MediaPlayer player = MediaPlayer.create(Global.application, Uri.parse(path));
        if (player == null) {
            return -1;
        }
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public String getCurrentPlayFile() {
        return currentPlayFile;
    }

    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }

    /**
     * 为了不更改EventListener以前的代码，暂时另写一个方法
     */
    public interface OnStartListener {
        void onStart();
    }

    /**
     * 播放器事件监听
     */
    public interface EventListener {
        void onStop();
    }
}
