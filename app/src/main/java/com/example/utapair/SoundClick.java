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
    private MediaPlayer mediaPlayerClick2;

    public SoundClick(Context context){
        mediaPlayerClick = MediaPlayer.create(context, R.raw.sc2);
        mediaPlayerClick2 = MediaPlayer.create(context, R.raw.sc2);
    }

    public SoundClick(Context context, Uri uriMedia,Uri uriMedia2){
        mediaPlayerClick = MediaPlayer.create(context, uriMedia);
        mediaPlayerClick2 = MediaPlayer.create(context, uriMedia2);
    }

    public void setMediaPlayerClick(Uri uriMedia,Uri uriMedia2) {
        this.mediaPlayerClick = MediaPlayer.create(context, uriMedia);
        this.mediaPlayerClick2 = MediaPlayer.create(context, uriMedia2);
    }

    /* method to play sound click */
    public void playSoundClick(){
        /* this method will have 2 mediaPlay
         * because when click button faster than
         * media mediaPlayer will not ready
         * for new sound to play */

        if(mediaPlayerClick.isPlaying()) {
            mediaPlayerClick2.start();
        }
        else {
            mediaPlayerClick.start();
        }

    }

    public void stopMediaPlayer(){
        if(mediaPlayerClick.isPlaying()) {
            mediaPlayerClick.stop();
        }
        if(mediaPlayerClick2.isPlaying()) {
            mediaPlayerClick2.stop();
        }
    }

    /* when we use object to play sound
     * it use memory when play sound a lot
     * we need to release mediaPLayer */
    public void releaseMediaPlayer(){
        mediaPlayerClick.release();
        mediaPlayerClick2.release();
    }
}
