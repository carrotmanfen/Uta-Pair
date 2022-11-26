package com.example.utapair;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import java.io.IOException;
/* this class is about mediaPLayer
* that have 2 media for play sound
* when click button faster than sound that play */
public class SoundClick{
    private Context context;
    private MediaPlayer mediaPlayerClick;

    public SoundClick(Context context){
        mediaPlayerClick = MediaPlayer.create(context, R.raw.sc2);
    }

    public SoundClick(Context context, Uri uriMedia){
        mediaPlayerClick = MediaPlayer.create(context, uriMedia);
    }

    public void setMediaPlayerClick(Uri uriMedia) {
        this.mediaPlayerClick = MediaPlayer.create(context, uriMedia);
    }

    /* method to play sound click */
    public void playSoundClick(){
        /* this method will have 2 mediaPlay
         * because when click button faster than
         * media mediaPlayer will not ready
         * for new sound to play */

            mediaPlayerClick.start();

    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    public void stopMediaPlayer(){
        mediaPlayerClick.stop();
    }
}
