package com.common.lib.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.common.lib.manager.ConfigurationManager;

import java.io.File;

public class MediaPlayerManager {

    private static final String TAG = "MediaPlayerManager";
    private static MediaPlayerManager mMediaPlayerManager;

    private MediaPlayer mPlayer;


    private MediaPlayerManager() {

    }


    public static MediaPlayerManager getInstance() {
        if (mMediaPlayerManager == null) {
            synchronized (TAG) {
                if (mMediaPlayerManager == null) {
                    mMediaPlayerManager = new MediaPlayerManager();
                }
            }
        }
        return mMediaPlayerManager;
    }

    public void playVoice(File file) {
        if (file.exists()) {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            mPlayer.reset();
            try {
                mPlayer.setDataSource(ConfigurationManager.Companion.getInstance().getContext(), Uri.fromFile(file));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepare();
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playVoice(Context context, int rawId) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawId);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSystemVoice(boolean isLoop) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);//系统自带提示音
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        try {
            mPlayer.setDataSource(ConfigurationManager.Companion.getInstance().getContext(), uri);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setLooping(isLoop);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void playVideo(File file, SurfaceView surfaceView) {
        if (file.exists()) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();

            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (mPlayer == null) {
                            mPlayer = new MediaPlayer();
                        }
                        mPlayer.reset();
                        mPlayer.setDataSource(ConfigurationManager.Companion.getInstance().getContext(), Uri.fromFile(file));
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                        mPlayer.setDisplay(holder);
                        mPlayer.setLooping(true);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }
    }

    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }
}
